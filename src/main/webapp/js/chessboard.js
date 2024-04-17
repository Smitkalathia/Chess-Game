document.addEventListener("DOMContentLoaded", function() {
    const board = document.querySelector('.chessboard');
    const gameID = 'uniqueGameID'; // This should be dynamically set per game instance
    const ws = new WebSocket('http://localhost:8080/WSChatServer-1.0-SNAPSHOT/chess.html');

    ws.onmessage = function(event) {
        const data = JSON.parse(event.data);
        updateBoardWithMoves(data.moves);
        updateGameStatus(data.status);
    };

    function setupBoard() {
        board.innerHTML = '';
        const letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];
        const initialSetup = [
            ['rook', 'knight', 'bishop', 'queen', 'king', 'bishop', 'knight', 'rook'],
            ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'],
            Array(8).fill(null),
            Array(8).fill(null),
            Array(8).fill(null),
            Array(8).fill(null),
            ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'],
            ['rook', 'knight', 'bishop', 'queen', 'king', 'bishop', 'knight', 'rook']
        ];

        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const square = document.createElement('div');
                const piece = initialSetup[row][col];
                const color = (row < 2) ? 'black' : 'white';
                square.classList.add(((row + col) % 2 === 0) ? 'white' : 'black');
                square.id = `cell-${row}-${col}`;
                square.setAttribute('draggable', true);
                square.addEventListener('dragstart', dragStart);
                square.addEventListener('dragover', allowDrop);
                square.addEventListener('drop', drop);

                if (piece) {
                    square.style.backgroundImage = `url('resources/${color}_${piece}.png')`;
                    square.style.backgroundSize = '75%';
                    square.style.backgroundRepeat = 'no-repeat';
                    square.style.backgroundPosition = 'center';
                }
                board.appendChild(square);
            }
        }
    }

    function dragStart(event) {
        event.dataTransfer.setData("text/plain", event.target.id);
        event.target.classList.add('dragging');
    }

    function allowDrop(event) {
        event.preventDefault();
    }

    function drop(event) {
        event.preventDefault();
        var sourceId = event.dataTransfer.getData("text");
        var targetId = event.target.id;

        let src = document.getElementById(sourceId);
        let tgt = event.target;

        if (tgt && src) {
            let moveData = `${src.id},${tgt.id}`;
            ws.send(moveData); // Send move to server via WebSocket
            src.classList.remove('dragging');
        }
    }

    function updateBoardWithMoves(moves) {
        // Clear previous highlights
        document.querySelectorAll('.possible-move').forEach(cell => {
            cell.classList.remove('possible-move');
        });

        // Highlight new possible moves
        Object.keys(moves).forEach(positionKey => {
            let positions = positionKey.split(',');
            let pieceCell = document.getElementById(`cell-${positions[0]}-${positions[1]}`);
            moves[positionKey].forEach(move => {
                let moveCell = document.getElementById(`cell-${move[0]}-${move[1]}`);
                if (moveCell) {
                    moveCell.classList.add('possible-move');
                }
            });
        });
    }

    function updateGameStatus(status) {
        const statusDiv = document.getElementById('game-status');
        statusDiv.textContent = status;
    }

    setupBoard(); // Initial setup of the board
});
