from app import db


class Transaction(db.Model):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    usd_amount = db.Column(db.Float)
    lbp_amount = db.Column(db.Float)
    usd_to_lbp = db.Column(db.Boolean)

    def __init__(self, usd_amount, lbp_amount, usd_to_lbp):
        super(Transaction, self).__init__(
            usd_amount=usd_amount, lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp
        )

    def toDict(self):
        return {
            "id": self.id,
            "usd_amount": self.usd_amount,
            "lbp_amount": self.lbp_amount,
            "usd_to_lbp": self.usd_to_lbp,
        }
