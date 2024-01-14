from app import db, bcrypt
from models.User import User, user_schema
from flask import request, Response, jsonify, Blueprint

import os
import jwt
import datetime


def create_token(user_id):
    payload = {
        "exp": datetime.datetime.utcnow() + datetime.timedelta(days=4),
        "iat": datetime.datetime.utcnow(),
        "sub": user_id,
    }
    return jwt.encode(payload, os.getenv("SECRET_KEY"), algorithm="HS256").decode(
        "utf-8"
    )


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


@user_bp.route("/authentication", methods=["POST"])
def authentication():
    user_name = request.json.get("user_name")
    password = request.json.get("password")

    if user_name is None or password is None:
        return Response(
            "{'Message': 'Invalid Input'}", status=400, mimetype="application/json"
        )
    user = User.query.filter_by(user_name=user_name).first()
    if user is None:
        return Response(
            "{'Message': 'Unauthorized access'}",
            status=403,
            mimetype="application/json",
        )
    if not bcrypt.check_password_hash(user.hashed_password, password):
        return Response(
            "{'Message': 'Authentication failed'}",
            status=403,
            mimetype="application/json",
        )
    token = create_token(user.id)
    return jsonify({"token": str(token)})
