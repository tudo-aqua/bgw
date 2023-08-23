package tools.aqua.bgw.elements.layoutviews

import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.elements.uicomponents.ButtonProps

external interface CameraPane : Props {
    var id: String
}

val ReactCameraPane = FC<ButtonProps> { props ->
    div {

    }
}

/*
<div class="container" id="items-container">
  <div class="target">
    <div class="item"></div>
  </div>
</div>


.container{
  overflow: hidden;
  position: absolute;
  width: 600px;
  height: 400px;
}
.item{
  width: 60px;
  height: 60px;
  background-color: red;
  position: absolute;
  left: 1200px;
  top: 100px;
}

.target {
  position: absolute;
  background: #333;
  width: 2000px;
  height: 2000px;
}

const container = document.querySelector('#items-container');

let startY;
let startX;
let scrollLeft;
let scrollTop;
let isDown;

container.addEventListener('mousedown',e => mouseIsDown(e));
container.addEventListener('mouseup',e => mouseUp(e))
container.addEventListener('mouseleave',e=>mouseLeave(e));
container.addEventListener('mousemove',e=>mouseMove(e));

function mouseIsDown(e){
  isDown = true;
  startY = e.pageY - container.offsetTop;
  startX = e.pageX - container.offsetLeft;
  scrollLeft = container.scrollLeft;
  scrollTop = container.scrollTop;
}
function mouseUp(e){
  isDown = false;
}
function mouseLeave(e){
  isDown = false;
}
function mouseMove(e){
  if(isDown){
    e.preventDefault();
    //Move vertcally
    const y = e.pageY - container.offsetTop;
    const walkY = y - startY;
    container.scrollTop = scrollTop - walkY;

    //Move Horizontally
    const x = e.pageX - container.offsetLeft;
    const walkX = x - startX;
    container.scrollLeft = scrollLeft - walkX;

  }
}
 */