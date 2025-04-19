from flask import Flask, jsonify

app = Flask(__name__)

anime_data = [
    "Naruto",
    "One Piece",
    "Attack on Titan",
    "Death Note",
    "Fullmetal Alchemist: Brotherhood",
    "Demon Slayer",
    "Jujutsu Kaisen",
    "My Hero Academia",
    "Steins;Gate",
    "Bleach"
]

@app.route('/anime', methods=['GET'])
def get_anime():
    print("GET /anime request received")
    return jsonify(anime_data)

if __name__ == '__main__':
    print("Starting Flask server on http://0.0.0.0:3000")
    app.run(host='0.0.0.0', port=3000, debug=True)