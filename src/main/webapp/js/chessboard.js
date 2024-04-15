document.addEventListener("DOMContentLoaded", function() {
    // Function to set up or reset the chessboard to its initial state
    function setupBoard() {
        const board = document.querySelector('.chessboard');
        board.innerHTML = '';  // Clear the existing board
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
                square.id = letters[col] + (8 - row);

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

    // Initial setup of the board when the page loads
    setupBoard();

    // Add event listener to the New Game button to reset the chessboard
    const newGameButton = document.querySelector('.new-game-btn');
    newGameButton.addEventListener('click', function(e) {
        e.preventDefault();  // Prevent the default action of the button (if it's a submit button or link)
        setupBoard();  // Reset the board
        console.log('Board reset to initial setup.');  // Log message to indicate the board was reset
    });
});
