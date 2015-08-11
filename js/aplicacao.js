(function () {
	'use strict';

	var projetosAplicacao = angular.module('projetosAplicacao', ['ngRoute']);

	projetosAplicacao.config(['$routeProvider', function ($routeProvider) {

		$routeProvider.when("/", {
			redirectTo: "/projetos"
		});

		$routeProvider.when("/projetos", {
			templateUrl: '/html/projetos',
			controller: 'ProjetosControle'
		});

		$routeProvider.when("/projetos/novo", {
			templateUrl: '/html/projetosNovo',
			controller: 'ProjetosNovoControle'
		});

		$routeProvider.when("/projetos/:caminho*", {
			templateUrl: '/html/projetos',
			controller: 'ProjetosControle'
		});

		$routeProvider.when("/projeto/:identificador*/arquivo/:caminho*", {
			templateUrl: '/html/projeto',
			controller: 'ProjetoControle'
		});

		$routeProvider.when("/projeto/:identificador*", {
			templateUrl: '/html/projeto',
			controller: 'ProjetoControle'
		});

		$routeProvider.when("/ajuda", {
			templateUrl: '/html/ajuda'
		});

	}]);
	
	projetosAplicacao.run(['$rootScope', '$route', function($rootScope, $route) {
        $rootScope.$on('$locationChangeStart', function() {
        	var projetos = /^#\/projetos/;
        	var anoAtual = /^#\/projetos\/ano2015/;
        	var semestreAtual = /^#\/projetos\/ano2015\/semestre2/;
        	if (projetos.test(location.hash)) {
        		$rootScope.pastaBase = location.hash.replace(semestreAtual, "").replace(anoAtual, "").replace(projetos, "").replace(/$/, "/").replace(/^\//, "");
        	} else {
        		$rootScope.pastaBase = "";
        	}
        });
	}]);

}());
