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
            ['rook', 'knight', 'bishop', 'king', 'queen', 'bishop', 'knight', 'rook'],
            ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'],
            Array(8).fill(null), Array(8).fill(null),
            Array(8).fill(null), Array(8).fill(null),
            ['pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn', 'pawn'],
            ['rook', 'knight', 'bishop', 'king', 'queen', 'bishop', 'knight', 'rook']
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
                    fetchImage(square, `${color}_${piece}`);
                }
                square.addEventListener('click', () => selectSquare(square));
            }
        }
    }

    function fetchImage(square, imageName) {
        const imageUrl = `http://localhost:8080/WSChatServer-1.0-SNAPSHOT/resources/${imageName}.png`;
        fetch(imageUrl)
            .then(response => {
                if (response.ok) {
                    // Set the image directly using the URL since the response is OK
                    square.style.backgroundImage = `url('${imageUrl}')`;
                    square.style.backgroundSize = "90% 90%";
                    square.style.backgroundRepeat = 'no-repeat';
                    square.style.backgroundPosition = 'center';
                } else {
                    throw new Error('Failed to fetch image');
                }
            })
            .catch(error => {
                console.error('Image load failed:', error);
            });
    }
    function selectSquare(square) {
        if (selectedSquare === square) {
            clearHighlights();
            square.classList.remove('selected');
            selectedSquare = null;
            return;
        }

        if (!square.style.backgroundImage && !selectedSquare) {
            console.log("Clicked on empty square");
            return;
        }

        if (selectedSquare) {
            if (isValidMove(selectedSquare, square)) {
                movePiece(selectedSquare, square);
                selectedSquare.classList.remove('selected');
                selectedSquare = null;
            } else {
                clearHighlights();
                selectedSquare.classList.remove('selected');
                selectedSquare = null;
            }
        }

        if (square.style.backgroundImage) {
            clearHighlights();
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
                    if (targetSquare.style.backgroundImage) {
                        if (isEnemyPiece(square, targetSquare)) {
                            targetSquare.classList.add('capture-move');
                        } else {
                            targetSquare.classList.add('possible-move');
                        }
                    } else {
                        targetSquare.classList.add('possible-move');
                    }
                }
            });
        }
    }
    function isEnemyPiece(fromSquare, toSquare) {
        const fromPiece = fromSquare.style.backgroundImage;
        const toPiece = toSquare.style.backgroundImage;
        return (fromPiece.includes('white') && toPiece.includes('black')) ||
            (fromPiece.includes('black') && toPiece.includes('white'));
    }
    function clearHighlights() {
        document.querySelectorAll('.chess-square.possible-move, .chess-square.capture-move').forEach(square => {
            square.classList.remove('possible-move');
            square.classList.remove('capture-move');
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

        clearHighlights();

        if (selectedSquare) {
            selectedSquare.classList.remove('selected');
            selectedSquare = null;
        }

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