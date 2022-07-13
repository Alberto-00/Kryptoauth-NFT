function timer() {
    var now = new Date();
    var start = new Date(now.getFullYear(), 0, 0);
    var diff = (now - start) + ((start.getTimezoneOffset() - now.getTimezoneOffset()) * 60 * 1000);
    var oneDay = 1000 * 60 * 60 * 24;
    var day = Math.floor(diff / oneDay);
    document.getElementById('days-count').innerHTML = day + '';
    document.getElementById('hours-count').innerHTML = now.getHours() + '';
    document.getElementById('minutes-count').innerHTML = now.getMinutes() + '';
    document.getElementById('seconds-count').innerHTML = now.getSeconds() + '';
    setTimeout('timer()', 1000);
}
timer();