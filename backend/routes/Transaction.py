from app import db
from models.Transaction import Transaction, transaction_schema, transactions_schema
from flask import request, Response, jsonify, Blueprint

import os
import jwt


def extract_auth_token(authenticated_request):
    auth_header = authenticated_request.headers.get("Authorization")
    if auth_header:
        return auth_header.split(" ")[1]
    else:
        return None


def decode_token(token):
    payload = jwt.decode(token, os.getenv("SECRET_KEY"), algorithms=["HS256"])
    return payload["sub"]


transaction_bp = Blueprint("transaction", __name__)


@transaction_bp.route("/transaction", methods=["POST"])
def transaction():
    usd_amount = request.json.get("usd_amount")
    lbp_amount = request.json.get("lbp_amount")
    usd_to_lbp = request.json.get("usd_to_lbp")
    if usd_amount is None or lbp_amount is None or usd_to_lbp is None:
        return Response(
            "{'Message': 'Invalid Input'}", status=400, mimetype="application/json"
        )
    auth_token = extract_auth_token(request)
    if auth_token is None:
        transaction = Transaction(usd_amount, lbp_amount, usd_to_lbp)
    else:
        try:
            user_id = decode_token(auth_token)
            transaction = Transaction(usd_amount, lbp_amount, usd_to_lbp, user_id)
        except Exception as e:
            print(str(e))
            return Response(
                "{'Message': 'Unauthorized access'}",
                status=403,
                mimetype="application/json",
            )
    db.session.add(transaction)
    db.session.commit()
    return jsonify(transaction_schema.dump(transaction))


@transaction_bp.route("/transaction", methods=["GET"])
def fetch_transaction():
    try:
        auth_token = extract_auth_token(request)
        user_id = decode_token(auth_token)
    except:
        return Response(
            "{'Message': 'Unauthorized access'}",
            status=403,
            mimetype="application/json",
        )
    transactions = Transaction.query.filter_by(user_id=user_id).all()
    return jsonify(transactions_schema.dump(transactions))
