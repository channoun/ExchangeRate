from flask import jsonify, Blueprint
from models.Transaction import Transaction

import datetime

exchange_bp = Blueprint("exchange", __name__)


@exchange_bp.route("/exchangeRate", methods=["GET"])
def exchange():
    END_TIME = datetime.datetime.now()
    START_TIME = END_TIME - datetime.timedelta(days=3)
    usd_to_lbp = Transaction.query.filter(
        Transaction.added_date.between(START_TIME, END_TIME),
        Transaction.usd_to_lbp == True,
    ).all()
    usd_to_lbp_average = None
    if len(usd_to_lbp) != 0:
        sm = 0
        for transaction in usd_to_lbp:
            sm += transaction.lbp_amount / transaction.usd_amount
        usd_to_lbp_average = sm / len(usd_to_lbp)
    lbp_to_usd = Transaction.query.filter(
        Transaction.added_date.between(START_TIME, END_TIME),
        Transaction.usd_to_lbp == False,
    ).all()
    lbp_to_usd_average = None
    if len(lbp_to_usd) != 0:
        sm = 0
        for transaction in lbp_to_usd:
            sm += transaction.lbp_amount / transaction.usd_amount
        lbp_to_usd_average = sm / len(lbp_to_usd)

    return jsonify(
        {
            "usd_to_lbp": usd_to_lbp_average,
            "lbp_to_usd": lbp_to_usd_average,
        }
    )
