var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var clicked = false;
var sonarPulseCount = 2;
var sonarPulse = false;

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function markHits(board, elementId, surrenderText) {
    board.sonars.forEach((sonar) => {
            let className;
            if (sonar.result === "MISS")
                className = "empty";
            else if (sonar.result === "HIT")
                className = "hasShip";
            document.getElementById(elementId).rows[sonar.location.row-1].cells[sonar.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });

    board.blocks.forEach((block) => {
        document.getElementById(elementId).rows[block.location.row-1].cells[block.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("miss");
    });

    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK")
            className = "sink"
        else if (attack.result === "SURRENDER"){
            alert(surrenderText);
             window.location.reload();
             }
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
        if(attack.result === "SUNK"){
            attack.ship.occupiedSquares.forEach((square) => {
                document.getElementById(elementId).rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("sink");
                });
                 if(elementId === "opponent")
                     document.getElementById("sonarPulse_button").classList.remove("hide");
                     document.getElementById("sonarPulse_counter").classList.remove("hide");
            }
    });
    if(elementId === "opponent") {
        attack = board.attacks[board.attacks.length - 1];
        if(typeof attack !== 'undefined') {
            let className;
                if (attack.result === "MISS")
                    className = "miss";
                else if (attack.result === "HIT")
                    className = "hit";
                else if (attack.result === "SUNK")
                    className = "sink"
            switch(board.lastAttack) {
                case 0:
                    document.getElementById("logText").innerHTML = className;
                    break;
                case 1:
                    document.getElementById("logText").innerHTML = "miss";
                    break;
                case 2:
                    document.getElementById("logText").innerHTML = "sonar";
                    break;
                default:
                    document.getElementById("logText").innerHTML = className;
            }
        }
    }
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    }));
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
}

var oldListener;
function registerCellListener(f) {
    let el = document.getElementById("player");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            redrawGrid();
            placedShips++;
            if (placedShips == 3) {
                isSetup = false;
                registerCellListener((e) => {});
            }
        });
    } else if (sonarPulse) {
              sendXhr("POST","/sonar", {game: game, x: row, y: col} , function(data){
                  game = data;
                  redrawGrid();
                  sonarPulse = false;
                  document.getElementById("sonarPulse_button").classList.remove("clicked");
                  sonarPulseCount = sonarPulseCount - 1;
                  var sonarPulseRemain = "SONAR PULSE REMAIN: " + sonarPulseCount.toString() ;
                  document.getElementById("sonarPulse_counter").innerHTML = sonarPulseRemain;
               });
    } else if (clicked) {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
            attackselected = false;
        });
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            alert("Cannot complete the action");
            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
    }
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4));
    });
    document.getElementById("place_submarine").addEventListener("click", function(e) {
        shipType = "SUBMARINE";
        registerCellListener(place(4));
    });
    document.getElementById("attack_button").addEventListener("click", function() {
        if(sonarPulse == false){
           this.classList.toggle("clicked");
           if(clicked){
              clicked = false;
           }
           else {
               clicked = true;
                }
        }
    });
    document.getElementById("surrender_button").addEventListener("click", function(e) {
          alert("You surrender. :<");
          window.location.reload();
        });
    document.getElementById("sonarPulse_button").addEventListener("click", function(){
            if(sonarPulseCount > 0){
                this.classList.toggle("clicked");
                if(sonarPulse){
                sonarPulse = false;
                }
                else {
                    sonarPulse = true;
                    if(clicked){
                        document.getElementById("attack_button").classList.remove("clicked");
                        clicked = false;
                    }
                }
            }
        });
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
    
    setTimeout(function(){
	    document.getElementById("notice").classList.add('hide');
    }, 10000);
};
