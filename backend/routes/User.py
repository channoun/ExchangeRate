from app import db
from models.User import User, user_schema
from flask import request, Response, jsonify, Blueprint

user_bp = Blueprint("user", __name__)


@user_bp.route("/user", methods=["POST"])
def create_user():
    user_name = request.json.get("user_name")
    password = request.json.get("password")

    if user_name is None or password is None:
        return Response(
            "{'Message': 'Invalid Input'}", status=400, mimetype="application/json"
        )
    user = User(user_name, password)
    db.session.add(user)
    db.session.commit()
    return jsonify(user_schema.dump(user))
