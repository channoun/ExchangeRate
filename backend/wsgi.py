from app import create_app
from routes.ExchangeRate import exchange_bp
from routes.Transaction import transaction_bp
from routes.User import user_bp

app = create_app()

app.register_blueprint(exchange_bp)
app.register_blueprint(transaction_bp)
app.register_blueprint(user_bp)
