document.addEventListener("DOMContentLoaded", function() {
    const board = document.querySelector('.chessboard');
    const gameID = 'uniqueGameID'; // Should be dynamically set per game instance
    const ws = new WebSocket(`ws://localhost:8080/chess/${gameID}`);

    ws.onmessage = function(event) {
        console.log("WebSocket message received:", event.data); // Debugging aid
        const data = JSON.parse(event.data);
        updateBoardWithMoves(data.moves);
        updateGameStatus(data.status);
    };

    function setupBoard() {
        board.innerHTML = ''; // Clear the board for initial setup
        const initialSetup = [
            ['rook', 'knight', 'bishop', 'queen', 'king', 'bishop', 'knight', 'rook'],
            ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'],
            Array(8).fill(null), Array(8).fill(null), Array(8).fill(null), Array(8).fill(null),
            ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'],
            ['rook', 'knight', 'bishop', 'queen', 'king', 'bishop', 'knight', 'rook']
        ];

        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const square = document.createElement('div');
                const piece = initialSetup[row][col];
                const color = (row < 2) ? 'black' : 'white';
                square.classList.add('chess-square', (row + col) % 2 === 0 ? 'white' : 'black');
                square.id = `cell-${row}-${col}`;
                square.setAttribute('draggable', true);
                square.addEventListener('dragstart', dragStart);
                square.addEventListener('dragover', allowDrop);
                square.addEventListener('drop', drop);

                if (piece) {
                    square.style.backgroundImage = `url('resources/${color}_${piece}.png')`;
                    square.style.backgroundSize = 'cover';
                    square.style.backgroundRepeat = 'no-repeat';
                    square.style.backgroundPosition = 'center';
                }
                board.appendChild(square);
            }
        }
    }

    function dragStart(event) {
        if (event.target.style.backgroundImage !== '') {
            event.dataTransfer.setData("text/plain", event.target.id);
            event.target.classList.add('dragging');
        }
    }

    function allowDrop(event) {
        event.preventDefault(); // Allows a drop
        if (event.target.className.includes('chess-square')) {
            event.target.style.opacity = '0.7'; // Visual cue for droppable area
        }
    }

    function drop(event) {
        event.preventDefault();
        const sourceId = event.dataTransfer.getData("text");
        const source = document.getElementById(sourceId);
        const target = event.target.closest('.chess-square'); // Ensures dropping on the square

        if (target && source && target !== source && target.classList.contains('possible-move')) {
            target.style.backgroundImage = source.style.backgroundImage;
            source.style.backgroundImage = '';
            ws.send(JSON.stringify({ from: sourceId, to: target.id }));
            source.classList.remove('dragging');
            document.querySelectorAll('.possible-move').forEach(cell => cell.classList.remove('possible-move'));
            target.style.opacity = '1'; // Reset opacity after drop
        }
    }

    function updateBoardWithMoves(moves) {
        // Clear previous highlights
        document.querySelectorAll('.chess-square').forEach(cell => {
            cell.classList.remove('possible-move', 'selected');
        });

        // Highlight new possible moves
        Object.entries(moves).forEach(([key, positions]) => {
            const pieceCell = document.getElementById(`cell-${key}`);
            if (pieceCell) {
                pieceCell.classList.add('selected');
                positions.forEach(([row, col]) => {
                    const moveCell = document.getElementById(`cell-${row}-${col}`);
                    if (moveCell) {
                        moveCell.classList.add('possible-move');
                    }
                });
            }
        });
    }

    function updateGameStatus(status) {
        const statusDiv = document.getElementById('game-status');
        statusDiv.textContent = status;
    }

    setupBoard(); // Initialize the chessboard
});
