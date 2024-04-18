let selectedSquare = null;
let movesData = {};
let ws;
//let curTurn = 'white';

document.addEventListener("DOMContentLoaded", function() {
    const board = document.querySelector('.chessboard');
    const gameID = 'SHA123';
    ws = new WebSocket(`ws://localhost:8080/WSChatServer-1.0-SNAPSHOT/chess/${gameID}`);
    console.log(`Web Socket connection on ws://localhost:8080/WSChatServer-1.0-SNAPSHOT/chess/${gameID} open`)

    ws.onmessage = function(event) {
        let data = JSON.parse(event.data);
        console.log("Moves data received:", data);
        if(data["moveToMake"]!==""){
            const move = data["moveToMake"];
            const key = Object.keys(move)[0];
            const fromRow = key.split(',')[0];
            const fromColumn = key.split(',')[1];
            const toRow = move[key].split(',')[0];
            const toColumn = move[key].split(',')[1];
            const fromSquare = document.getElementById(`${fromRow}-${fromColumn}`);
            const toSquare = document.getElementById(`${toRow}-${toColumn}`);
            movePieceWithoutSend(fromSquare, toSquare);
        }
        movesData = data["possibleMoves"];

    };
    function setupBoard() {
        board.innerHTML = '';
        const initialSetup = [
            ['rook', 'knight', 'bishop', 'queen', 'king', 'bishop', 'knight', 'rook'],
            ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'],
            Array(8).fill(null), Array(8).fill(null),
            Array(8).fill(null), Array(8).fill(null),
            ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'],
            ['rook', 'knight', 'bishop', 'queen', 'king', 'bishop', 'knight', 'rook']
        ];

        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const square = document.createElement('div');
                square.classList.add('chess-square', (row + col) % 2 === 0 ? 'white' : 'black');
                square.id = `${row}-${col}`;
                board.appendChild(square);
                const piece = initialSetup[row][col];
                if (piece) {

                    const color = row < 2 ? 'white' : (row >= 6 ? 'black' : '');
                    square.style.backgroundImage = `url('resources/${color}_${piece}.png')`;
                    square.style.backgroundSize = 'cover';
                    square.style.backgroundRepeat = 'no-repeat';
                    square.style.backgroundPosition = 'center';
                }
                square.addEventListener('click', () => selectSquare(square));
            }
        }
    }
    function selectSquare(square) {
        if (!square.style.backgroundImage && !selectedSquare) {
            console.log("Clicked on empty square");
            return;
        }

        if (selectedSquare) {
            if (isValidMove(selectedSquare, square)) {
                movePiece(selectedSquare, square);
                clearHighlights();
                selectedSquare.classList.remove('selected');
                selectedSquare = null;
                return;
            } else {
                clearHighlights();
                selectedSquare.classList.remove('selected');
                selectedSquare = null;
            }
        }

        if (square.style.backgroundImage) {
            selectedSquare = square;
            square.classList.add('selected');
            highlightPossibleMoves(square);
        }
    }


    function highlightPossibleMoves(square) {
        const [row, col] = square.id.split('-').map(Number);
        const fromKey = `${row},${col}`;
        const possibleMoves = movesData[fromKey];

        if (possibleMoves) {
            possibleMoves.forEach(move => {
                const [moveRow, moveCol] = move;
                const targetSquare = document.getElementById(`${moveRow}-${moveCol}`);
                if (targetSquare) {
                    targetSquare.classList.add('possible-move');
                }
            });
        }
    }

    function clearHighlights() {
        document.querySelectorAll('.chess-square.possible-move').forEach(square => {
            square.classList.remove('possible-move');
        });
    }

    function movePiece(fromSquare, toSquare) {
        // Update the local board state
        toSquare.style.backgroundImage = fromSquare.style.backgroundImage;
        fromSquare.style.backgroundImage = '';

        toSquare.style.backgroundSize = 'cover';
        toSquare.style.backgroundRepeat = 'no-repeat';
        toSquare.style.backgroundPosition = 'center';

        fromSquare.style.backgroundSize = '';
        fromSquare.style.backgroundRepeat = '';
        fromSquare.style.backgroundPosition = '';

        movesData = {};
        const moveData = {};
        const fromKey = fromSquare.id.toString().replace('-', ',');
        moveData[fromKey] = toSquare.id.toString().replace('-', ',');
        console.log(moveData);
        ws.send(JSON.stringify(moveData));
    }
    function movePieceWithoutSend(fromSquare, toSquare) {
        // Update the local board state
        toSquare.style.backgroundImage = fromSquare.style.backgroundImage;
        fromSquare.style.backgroundImage = '';

        toSquare.style.backgroundSize = 'cover';
        toSquare.style.backgroundRepeat = 'no-repeat';
        toSquare.style.backgroundPosition = 'center';

        fromSquare.style.backgroundSize = '';
        fromSquare.style.backgroundRepeat = '';
        fromSquare.style.backgroundPosition = '';
    }

    function isValidMove(fromSquare, toSquare) {
        const fromRow = fromSquare.id.split('-')[0];
        const fromCol = fromSquare.id.split('-')[1];
        const toRow = toSquare.id.split('-')[0];
        const toCol = toSquare.id.split('-')[1];
        const fromKey = `${fromRow},${fromCol}`;
        const possibleMoves = movesData[fromKey];

        console.log(`Validating move from ${fromKey} to ${toRow}-${toCol}`, possibleMoves);

        if (possibleMoves) {
            return possibleMoves.some(move => move[0] === parseInt(toRow) && move[1] === parseInt(toCol));
        }
        return false;
    }
    setupBoard();
});