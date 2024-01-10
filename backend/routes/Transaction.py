from app import db
from models.Transaction import Transaction
from flask import request, Response, jsonify, Blueprint

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
    transaction = Transaction(usd_amount, lbp_amount, usd_to_lbp)
    db.session.add(transaction)
    db.session.commit()
    return jsonify(transaction.toDict())
