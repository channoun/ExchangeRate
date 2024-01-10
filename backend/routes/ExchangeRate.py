from flask import jsonify, Blueprint
from models.Transaction import Transaction

exchange_bp = Blueprint("exchange", __name__)


@exchange_bp.route("/exchangeRate", methods=["GET"])
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
