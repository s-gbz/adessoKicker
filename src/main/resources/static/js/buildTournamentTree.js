var numBoxes;
var currentLevel;

//writes tournament name and start date to the headline
function setHeadline () {
	
	document.getElementById('heading').innerHTML = tournamentName + ' - ' + tournamentStartDate.slice(8, 10) + '.' + tournamentStartDate.slice(5, 7) + '.' + tournamentStartDate.slice(0, 4);
	
}

//finds out how many boxes have to be created on the first row
function setNumBoxes () {
	
	var num = 2;
	
	while (rows[0].length > num)
	{
		num *= 2;
	}
	
	numBoxes = num;
}

//finds out how many rows have to be created
function setCurrentLevel () {
	
	currentLevel = 0;
	var rowHasTeam = true;
	var i;
	var j;
	
	//ilterates through each row of teams
	for(i = 1; i < numRows; i++){
			
		//check if row has any team 
		if(rows[i].length != 0){
				
			currentLevel ++;
		}
		
		else {
			
			break;
		}
	}
}

//creates and attaches teamboxes with teamnames to the page
function printTree (){
	
	var i;
	var j; 
	var teamBoxes = "";
	var level = "";
	var	posx;
	
	var boxStyle = "border-radius: 5px; background-color: white; border-style: groove; padding: 5px; margin: 3px; box-shadow: 2px 2px 1px black;";
	var divEvents = "onmouseover='onMouseInteractDiv(this)' onmouseout='outMouseInteractDiv(this)'";
	
	//ilterates through each row of teams
	for(i = 0; i <= currentLevel; i ++){
		
		
		level = '<div class="container" id="level' + i + '" style="display: flex; flex-flow: row nowrap; justify-content: space-between; z-index: 5;">'
		teamBoxes = "";
		
		//ilterates through each team in a row
		for(j = 0; j < (numBoxes / Math.pow(2, i)); j++) {
			
			//creates a box with a team in it
			if(j < rows[i].length) {
				
				//if box is not in the first row its x-position is set between the two parent boxes
				if(i > 0) {
				
					posx = (returnCenter(document.getElementById('level' + (i - 1) + 'team' + (j * 2))) + returnCenter(document.getElementById('level' + (i - 1) + 'team' + ((j * 2) + 1)))) / 2;
					teamBoxes += '<div class="" id="level' + i + 'team' + j +'" ' + divEvents + 'onclick="javacript:location.href=\'/teams/' + rows[i][j].teamId + '\' style="left: ' + posx + 'px; ' + boxStyle + ' position: absolute;" >' + rows[i][j].teamName + '</div>';
				}
				//box position is set automatically
				else {
					teamBoxes += '<div class="" id="level' + i + 'team' + j +'" ' + divEvents + ' onclick="javacript:location.href="/teams/' + rows[i][j].teamId + '"  style="' + boxStyle + '" >' + rows[i][j].teamName + '</div>';
				}
			}
			
			//creates a box with no team in it
			else {
				
				//if box is not in the first row its x-position is set between thw two parent boxes
				if(i > 0) {
				
					posx = ((document.getElementById('level' + (i - 1) + 'team' + (j * 2)).getBoundingClientRect().right + document.getElementById('level' + (i - 1) + 'team' + ((j * 2) + 1)).getBoundingClientRect().left) / 2);
					teamBoxes += '<div class="" id="level' + i + 'team' + j +'" ' + divEvents + ' style="left: ' + posx + 'px; ' + boxStyle + ' position: absolute;" ></div>';
				}
				//box position is set automatically
				else {
					teamBoxes += '<div class="" id="level' + i + 'team' + j +'" ' + divEvents + ' style="' + boxStyle + '" ></div>';
				}
			}
		}
		
		//attaches row of teams to the page
		document.getElementById('tree').innerHTML += level + teamBoxes + '</div><br><br><br><br><br>';
	}
}

//draws lines between the teamboxes
function printLines () {
	
	var team1;
	var team2;
	var target; 
	var canvas; 
	var ctx;
	var fixlx; 
	var fixly; 
	var fixrx; 
	var fixry; 
	var i;
	var j;
	var yOffset = 234;
	var centerSpacing = 180;
	
	canvas = document.getElementById('canvas');
	ctx = canvas.getContext("2d");
	
	//draw line to display a match pair
	for(i = 0; i <= currentLevel; i++) {
		
		for(j = 0; j < rows[i].length / 2; j++) {
		
			team1 = document.getElementById('level' + i + 'team' + (j * 2));
			team2 = document.getElementById('level' + i + 'team' + ((j * 2) + 1));
			
			//get position of first team
			fixlx = returnCenter(team1);
			fixly = team1.getBoundingClientRect().bottom - centerSpacing;
					
			//get position of second team
			fixrx = returnCenter(team2);
			fixry = team2.getBoundingClientRect().bottom - centerSpacing;
			
			ctx.moveTo(fixlx, team1.getBoundingClientRect().bottom - yOffset);
			ctx.lineTo(fixlx, fixly);
			ctx.moveTo(fixrx, team2.getBoundingClientRect().bottom - yOffset);
			ctx.lineTo(fixrx, fixry);
			ctx.moveTo(fixlx, fixly);
			ctx.lineTo(fixrx, fixry);
			
			ctx.stroke();
		}
	}
	
	//ilterates through each row of teams
	for(i = 0; i < currentLevel; i ++){
		
		//ilterates through each team in a row
		for(j = 0; j < (numBoxes / Math.pow(2, i)); j++) {
	
			//get elements from the page and sets cursor up to draw
			team1 = document.getElementById('level' + i + 'team' + (j * 2));
			team2 = document.getElementById('level' + i + 'team' + ((j * 2) + 1));
			target = document.getElementById('level' + (i + 1) + 'team' + j);
			
			//get position of first team
			fixlx = returnCenter(team1);
			fixly = team1.getBoundingClientRect().bottom - centerSpacing;
			
			//get position of second team
			fixrx = returnCenter(team2);
			fixry = team2.getBoundingClientRect().bottom - centerSpacing;
			
			//draw lines to target team in next row
			ctx.moveTo(returnCenter(target), fixly);
			ctx.lineTo(returnCenter(target), target.getBoundingClientRect().top - yOffset);
			ctx.stroke();
		
		}
	}
}

//gets the center of an element
function returnCenter (element) {
	
	return ((element.getBoundingClientRect().left + element.getBoundingClientRect().right) / 2);
}

setNumBoxes();
setCurrentLevel();
setHeadline();
printTree();
printLines();
