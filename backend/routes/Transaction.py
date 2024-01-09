from app import app, db
from flask import request, Response, jsonify
from models.Transaction import Transaction


@app.route("/transaction", methods=["POST"])
def transaction():
    id = request.json["id"]
    usd_amount = request.json["usd_amount"]
    lbp_amount = request.json["lbp_amount"]
    usd_to_lbp = request.json["usd_tolbp"]
    if not id or not usd_amount or not lbp_amount or not usd_to_lbp:
        return Response(
            "{'Message': 'Invalid Input'}", status=400, mimetype="application/json"
        )
    transaction = Transaction(id, usd_amount, lbp_amount, usd_to_lbp)
    db.session.add(transaction)
    db.session.commit()
    return jsonify(transaction)
