let ws;

function newRoom() {
    let callURL = "http://localhost:8080/WSChatServer-1.0-SNAPSHOT/chat-servlet";
    fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept': 'text/plain',
        },
    })
        .then(response => response.text())
        .then(roomID => enterRoom(roomID, "new"));
}

function enterRoom(roomID, status) {
    document.getElementById("section-inner").innerHTML = `<h2>You are chatting in room: ${roomID}</h2>`;

    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.close();
    }

    ws = new WebSocket(`ws://localhost:8080/WSChatServer-1.0-SNAPSHOT/ws/${roomID}`);

    refreshRoomList(status);

}

function refreshRoomList(status) {
    if (ws) {
        ws.onopen = () => {
            if (status === "new") {
                document.getElementById("log").value = '';

                let req = {type: "codes", msg: ""};
                ws.send(JSON.stringify(req));
            }
        };
    } else if (!ws && status === "refresh"){
        ws = new WebSocket(`ws://localhost:8080/WSChatServer-1.0-SNAPSHOT/ws/`);
        ws.onopen = () => {
            document.getElementById("log").value = '';

            let req = {type: "codes", msg: ""};
            ws.send(JSON.stringify(req));
        };
    }

    ws.onmessage = event => {
        console.log(event.data);
        let msg = JSON.parse(event.data);
        if (msg.type === "codes") {
            let rooms = msg.message;
            let roomContainer = document.getElementById('room-buttons');
            roomContainer.innerHTML = '';

            rooms.forEach(roomID => {
                if (roomID.length >= 5) {
                    let button = document.createElement('button');
                    button.textContent = roomID;
                    button.classList.add('button', 'room-item');
                    button.onclick = () => {
                        document.getElementById("log").value = '';
                        enterRoom(roomID, "existing");
                    }
                    roomContainer.appendChild(button);
                }
            });
        } else {
            document.getElementById("log").value += "[" + timestamp() + "] " + msg.message + "\n";
        }
    };
}

function send() {
    let input = document.getElementById("input");

    let req = {"type": "chat", "msg": input.value};
    ws.send(JSON.stringify(req));
    input.value = "";
}

/*
edit to try n send images, allowed for me to actually add the image, but could not
send it through

function send() {
    let input = document.getElementById("input");
    let imageInput = document.getElementById("imageInput").files[0];

    let req = {"type": "chat", "msg": input.value};

    if(imageInput) {
        let reader = new FileReader();
        reader.onload = function(event) {
            req.image = event.target.result;
            ws.send(JSON.stringify(req));
        };
        reader.readAsDataURL(imageInput);
    } else {
        ws.send(JSON.stringify(req));
    }

    input.value = "";
    document.getElementById("imageInput").value = "";
}
 */

function timestamp() {
    let d = new Date(), minutes = d.getMinutes();
    if (minutes < 10) minutes = '0' + minutes;
    return d.getHours() + ':' + minutes;
}

document.getElementById("input").addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        event.preventDefault();
        send();
    }
});
// Refreshes on load
document.addEventListener("DOMContentLoaded", function() {
    refreshRoomList("refresh");
});
// Refreshes when Refresh is clicked
document.getElementById("refreshButton").addEventListener("click", function() {
    location.reload();
});