from flask import Flask
from dotenv import load_dotenv
from urllib.parse import quote_plus
from flask_sqlalchemy import SQLAlchemy

import os

app = Flask(__name__)

load_dotenv()

app.config[
    "SQLALCHEMY_DATABASE_URI"
] = f"mysql+pymysql://{os.getenv('MYSQL_USERNAME')}:{quote_plus(os.getenv('MYSQL_PASSWORD'))}@localhost:3306/exchange"

db = SQLAlchemy(app)

# Uncomment for unit testing
app.app_context().push()


@app.route("/hello", methods=["GET"])
def hello_world():
    return "Hello World!"
