const slides = document.querySelectorAll(".item");

let current = 0;
let prev = 2;
let next = 1;

const gotoNext = () => current < 2 ? gotoNum(current + 1) : gotoNum(0);

function gotoNum(number) {
    current = number;
    prev = current - 1;
    next = current + 1;

    for (let i = 0; i < slides.length; i++) {
        slides[i].classList.remove("active");
        slides[i].classList.remove("prev");
        slides[i].classList.remove("next");
    }

    if (next === 3){
        next = 0;
    }
    if (prev === -1){
        prev = 2;
    }

    slides[current].classList.add("active");
    slides[prev].classList.add("prev");
    slides[next].classList.add("next");
}

let counter = 0;
var i = setInterval(function(){
    
    gotoNext();

    counter++;
    if(counter === 20) {
        clearInterval(i);
    }
}, 3000);




