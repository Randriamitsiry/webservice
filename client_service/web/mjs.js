$("#ajouter").on("click", function (e) {
        var $id = $("#id").val();
        var $nom = $("#nom").val();
        var $prenom = $("#prenom").val();
        $.ajax({
            url: 'http://localhost:8080/client_service/webresources/test/entretien/client',
            dataType: "json",
            type: "GET",
            contentType: 'application/x-www-form-urlencoded; charset=utf-8',
            data: { id: $id, nom: $nom, prenom: $prenom },
            async: false,
            processData: true,
            cache: false,
            success: function (data) {
                alert(data.table);
                $.each(data,function(n){alert(n);});
            },
            error: function (xhr) {
                alert('error');
            }
        
 });
/*
 * $.ajax({
            url: 'http://localhost:8080/client_service/webresources/test/connect/entretien',
            dataType: "json",
            type: "POST",
            contentType: 'application/x-www-form-urlencoded; charset=utf-8',
            //data: { id: $id, nom: $nom, prenom: $prenom },
            async: false,
            processData: true,
            cache: false,
            success: function (data) {
                alert(data);
            },
            error: function (xhr) {
                alert('error');
            }
        });
 * 
 * function resultat(mbpr,mbli,nbom)
{
    if (mbpr == 0) {
        $("#span-produit").css("display", "none");
    }
    else {
        $("#span-produit").css("display", "inline");
    }
    if (mbli == 0) {
        $("#span-client").css("display", "none");
    }
    else {
        $("#span-client").css("display", "inline");
    }
    if (nbom == 0) {
        $("#span-commande").css("display", "none");
    }
    else {
        $("#span-commande").css("display", "inline");
    }
    $("#badge-produit").text(mbpr);
    $("#badge-client").text(mbli);
    $("#badge-commande").text(nbom);
}
$(document).ready(function () {
    resultat(0, 0, 0);
    $(".hovered").on("mouseover", function (e) {
        var height, width;
        var show = $(".show");
        var parent = $(this);
        height = parent.height();
        width = parent.width();
        e.preventDefault();
        $(".show").removeClass("show");
        $(this).prev().addClass("show").css({"width":width+"px","height":height+"px"});
    });
    $(".abs").on("mouseout", function (e) {
        e.preventDefault();
        $(".show").removeClass("show");
    });
    $("#supprimer").on("click", function (e) {
        e.preventDefault();
        $("#myModal").modal();
    });
    $("#ajout_panier").on("click", function (e) {
        e.preventDefault();
        $("#modal_ajout_panier").modal();
    });
    $(".ajout_panier").on("click", function (e) {
        e.preventDefault();
        var id = $(this).attr("href");
        $(id).modal();
    });

    $("#vider , #valider , #supprimer_panier, #modifier_panier").on("click", function (e) {
        e.preventDefault();
        var id = $(this).attr("href");
        $(id).modal();
    });

    $("#ajoutProduit").on("submit", function (e) {
        if($("#inputNom").val() == "")e.preventDefault();
        else if($("#inputPassword").val()=="")
        {
            $("#inputPassword").val("Unite");
        }
    });
    $('.datepicker').datepicker({
        autoclose: true,
        format: "yyyy-mm-dd",
        todayHighlight: true,
        orientation: "top auto",
        todayBtn: true
    });
    


    $('input[type="number"]').on("keyup", function (e) {
        var $this = $(this);
        var $nb = parseFloat($this.val());
        if(isNaN($nb) || $nb==0)
        {
            $this.val("");
        }
    });

    $('input[type="date"]').on("keyup", function (e) {
        $(this).val("");
    });
});
function e(ids) {
    var ret;
    $.ajax({
        url: '/Produit/verifierQte',
        dataType: "json",
        type: "GET",
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        data: { id: ids, qte: $('.qte-produit_'+ids).val() },
        async: false,
        processData: true,
        cache: false,
        success: function (data) {
            if(data)
            {
                ret=true;
            }
            else
            {
                $('.qte-produit_' + ids).val("Quantite en stock insuffisante");
                ret = false;
            }
        },
        error: function (xhr) {
            alert('error');
        }
    });
    return ret;
}

 */
});