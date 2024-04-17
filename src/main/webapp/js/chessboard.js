document.addEventListener("DOMContentLoaded", function() {
    const board = document.querySelector('.chessboard');
    const gameID = 'uniqueGameID'; // This should be dynamically set per game instance
    const ws = new WebSocket(`ws://localhost:8080/chess/${gameID}`);
    let selectedPiece = null;  // To keep track of the currently selected chess piece

    ws.onmessage = function(event) {
        const data = JSON.parse(event.data);
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
                square.classList.add('chess-square', (row + col) % 2 === 0 ? 'white' : 'black');
                square.id = `cell-${row}-${col}`;
                board.appendChild(square);
                const piece = initialSetup[row][col];
                if (piece) {
                    const color = row < 2 ? 'black' : (row >= 6 ? 'white' : '');
                    square.style.backgroundImage = `url('resources/${color}_${piece}.png')`;
                    square.style.backgroundSize = 'cover';
                    square.style.backgroundRepeat = 'no-repeat';
                    square.style.backgroundPosition = 'center';
                }
            }
        }
    }

    function handleSquareClick(event) {
        const square = event.target.closest('.chess-square');
        if (!square) return;
        if (square.style.backgroundImage !== '') {
            if (selectedPiece === square) {
                clearSelection();
            } else {
                clearSelection(); // Clear any previous selection
                selectedPiece = square;
                square.classList.add('selected');
                showPossibleMoves(square.id);
            }
        } else if (selectedPiece && square.classList.contains('possible-move')) {
            movePiece(selectedPiece, square);
            selectedPiece = null;
        }
    }

    function showPossibleMoves(pieceId) {
        console.log(`Showing possible moves for ${pieceId}`);
        document.querySelectorAll('.chess-square').forEach(cell => {
            if (Math.random() > 0.9) { // Replace this placeholder logic with actual move logic
                cell.classList.add('possible-move');
            }
        });
    }

    function movePiece(fromSquare, toSquare) {
        toSquare.style.backgroundImage = fromSquare.style.backgroundImage;
        fromSquare.style.backgroundImage = '';
        ws.send(JSON.stringify({ from: fromSquare.id, to: toSquare.id }));
        console.log(`Moved from ${fromSquare.id} to ${toSquare.id}`);
        clearSelection();
    }

    function clearSelection() {
        document.querySelectorAll('.selected, .possible-move').forEach(cell => {
            cell.classList.remove('selected', 'possible-move');
        });
        selectedPiece = null;
    }

    function updateGameStatus(status) {
        const statusDiv = document.getElementById('game-status');
        if (statusDiv) {
            statusDiv.textContent = status;
        } else {
            console.error('Game status element not found.');
        }
    }

    setupBoard(); // Initialize the chessboard
});
