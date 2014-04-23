var games = [];

function Game(sport, time, inprogress, opponent, location, scores){
    this.sport = sport;
    this.time = time;
    this.inprogress = inprogress;
    this.opponent = opponent;
    this.location = location;
    this.scores = scores || [0,0];
    games.push(this);
}
Game.prototype.toAbstract = function(){
    var html =
        '<div class="game">'+
            '<div class="sport">' + this.sport + '</div>' +
            '<div class="chevron"><i class="fa fa-chevron-right"></i></div><br>' +
            '<div class="teams">';
    if(this.inprogress){
        html += 'UPrep <span class="badge">'+this.scores[0]+'</span>  '+this.opponent+' <span class="badge">'+this.scores[1]+'</span>';
    } else {
        html += 'Vs. '+this.opponent+' - Tomorrow 3:00, '+this.location;
    }
    html += '</div>' +
        '</div>';
    return html;
}
Game.prototype.toDetail = function(){
    var html =
        '<div class="page-header"><h3>'+this.sport+'</h3></div>';
    return html;
}

function updateOverview(){
    var inprogress = $(".inprogress");
    var upcoming = $(".upcoming");
    inprogress.html("");
    upcoming.html("");
    var sorted = _.sortBy(games, function(item){ item.time });
    _.each(sorted, function(game){
        var entry = $(game.toAbstract()).data({game: game});
        if(game.inprogress)
            entry.appendTo(inprogress);
        else
            entry.appendTo(upcoming);
    });
}

$(document).ready(function(){
    new Game("Varsity Girls Soccer", new Date(), true, 'Lakeside', 'Away', [3,0]);
    new Game("Varsity Boys Ultimate", new Date(0), false, 'Northwest', 'Home');
    updateOverview();
    $(".upcoming, .inprogress").on("click", ".game", function(e){
        var game = $(this).data().game;
        $(".container.detail").html(game.toDetail()).animate({left: 0}, 200);
    });
    $("[href=#overview]").click(function(){
        $(".container.detail").animate({left: window.innerWidth},200, function(){
            $(".container.detail").css({left: ''});
        });
    });
});
