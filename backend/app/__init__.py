from flask import Flask
from flask_cors import CORS
from dotenv import load_dotenv
from urllib.parse import quote_plus
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow

import os

db = SQLAlchemy()
ma = Marshmallow()


def create_app():
    app = Flask(__name__)
    CORS(app)

    load_dotenv()

    app.config[
        "SQLALCHEMY_DATABASE_URI"
    ] = f"mysql+pymysql://{os.getenv('MYSQL_USERNAME')}:{quote_plus(os.getenv('MYSQL_PASSWORD'))}@localhost:3306/exchange"

    db.init_app(app)
    ma.init_app(app)

    # Uncomment for unit testing
    # app.app_context().push()

    return app
