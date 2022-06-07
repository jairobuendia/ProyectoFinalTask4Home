import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  {
  title = 'Task4HomeLanding';

  myFunction() {
    var x = document.getElementById("myTopnav");
    if (x !== null){
      if (x.className === "topnav") {
        x.className += " responsive";
      } else {
        x.className = "topnav";
      }
    }
    
  }

  playSound(){
    var audio = new Audio("../assets/sound/mario-coin.mp3")
    audio.play();
  }
}


