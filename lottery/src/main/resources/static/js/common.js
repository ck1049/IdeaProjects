function randomColor() {
    var ox = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'];
    var color = "#";
    for (let i = 0; i < 6; i++) {
        let number = Math.floor(Math.random()*16);
        color += ox[number];
    }
    return color
}