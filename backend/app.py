from dotenv import load_dotenv
from urllib.parse import quote_plus
from flask_sqlalchemy import SQLAlchemy
from flask import Flask, request, Response, jsonify

import os
import jsonpickle

app = Flask(__name__)

load_dotenv()

app.config[
    "SQLALCHEMY_DATABASE_URI"
] = f"mysql+pymysql://{os.getenv('MYSQL_USERNAME')}:{quote_plus(os.getenv('MYSQL_PASSWORD'))}@localhost:3306/exchange"

db = SQLAlchemy(app)


class Transaction(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    usd_amount = db.Column(db.Float)
    lbp_amount = db.Column(db.Float)
    usd_to_lbp = db.Column(db.Boolean)

    def __init__(self, id, usd_amount, lbp_amount, usd_to_lbp):
        super(Transaction, self).__init__(
            id=id, usd_amount=usd_amount, lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp
        )

    def toDict(self):
        return {
            "id": self.id,
            "usd_amount": self.usd_amount,
            "lbp_amount": self.lbp_amount,
            "usd_to_lbp": self.usd_to_lbp,
        }


# Uncomment for unit testing
# app.app_context().push()


@app.route("/hello", methods=["GET"])
def hello_world():
    return "Hello World!"


@app.route("/transaction", methods=["POST"])
def transaction():
    id = request.json.get("id")
    usd_amount = request.json.get("usd_amount")
    lbp_amount = request.json.get("lbp_amount")
    usd_to_lbp = request.json.get("usd_to_lbp")
    if id is None or usd_amount is None or lbp_amount is None or usd_to_lbp is None:
        return Response(
            "{'Message': 'Invalid Input'}", status=400, mimetype="application/json"
        )
    transaction = Transaction(id, usd_amount, lbp_amount, usd_to_lbp)
    db.session.add(transaction)
    db.session.commit()
    return jsonify(transaction.toDict())


@app.route("/exchangeRate", methods=["GET"])
def exchange():
    usd_to_lbp = Transaction.query.filter_by(usd_to_lbp=1).all()
    usd_to_lbp_average = None
    if len(usd_to_lbp) != 0:
        usd, lbp = 0, 0
        for transaction in usd_to_lbp:
            usd += transaction.usd_amount
            lbp += transaction.lbp_amount
        usd_to_lbp_average = lbp / usd
    lbp_to_usd = Transaction.query.filter_by(usd_to_lbp=0).all()
    lbp_to_usd_average = None
    if len(lbp_to_usd) != 0:
        usd, lbp = 0, 0
        for transaction in lbp_to_usd:
            usd += transaction.usd_amount
            lbp += transaction.lbp_amount
        lbp_to_usd_average = usd / lbp

    return jsonify({"usd_to_lbp": usd_to_lbp_average, "lbp_to_usd": lbp_to_usd_average})
