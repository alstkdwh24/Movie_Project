$.ajax({
    type:"get",
    url:"/getMovieCategory",
    success:function (data){
        console.log(data);
        movie_category_create(data);

    },
    error:function(err,status){
        console.log(err,status);
        alert('카테고리를 불러오는데 실패하였습니다. F5를 눌러서 새로고침을 해주세요');
    }
});
function getMovieCategory_List(e){
    e.preventDefault();
    if(e.target.tagName !="A") return;
    let dataset=$(e.target).data("set");


 $(e.currentTarget).find("a").removeClass("");
    $(e.target).addClass("");

    if(dataset.movie_category_lv||dataset.movie_category_lv){
        console.log("1lv");
        $().loading();
        $(e.currentTarget).movie_category_remove();
        movie_category_create();

        $.ajax({
            type:"get",
            url:"/getMovieCategoryChild" +dataset.group_id + "/"+ dataset.movie_category_lv +"/"+ dataset.movie_category_detail_lv,
               success: function(data){
                console.log(data);
                movie_category_create(data);

               },
               error: function(err, status){
                console.log(err,status);
               }
        });


        $(e.target).movie_category_set();

    }

}
$.fn.movie_category_set= function(){
    let movie_category_id= this.data("set").movie_category_id;
    let group_id= this.data("set").group_id;
    $("")
}
$.fn.movie_category_remove=function (){
    while(this.next().length !=0){
        $(this).next().remove();
    }

    function movie_category_create(data){
        console.log(data);
        if(!Array.isArray(data)){
            console.log("data is not an array or is undefine:", data);
            return;
        }

    }
}