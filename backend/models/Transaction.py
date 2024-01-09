from app import db


class Transaction(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    usd_amount = db.Column(db.Float)
    lbp_amount = db.Column(db.Float)
    usd_to_lbp = db.Column(db.Boolean)

    def __init__(self, id, usd_amount, lbp_amount, usd_to_lbp):
        self.id = id
        self.usd_amount = usd_amount
        self.lbp_amount = lbp_amount
        self.usd_to_lbp = usd_to_lbp
