from app import db, ma


class Transaction(db.Model):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    usd_amount = db.Column(db.Float)
    lbp_amount = db.Column(db.Float)
    usd_to_lbp = db.Column(db.Boolean)

    def __init__(self, usd_amount, lbp_amount, usd_to_lbp):
        super(Transaction, self).__init__(
            usd_amount=usd_amount, lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp
        )


class TransactionSchema(ma.Schema):
    class Meta:
        fields = ("id", "usd_amount", "lbp_amount", "usd_to_lbp")
        model = Transaction


transaction_schema = TransactionSchema()
