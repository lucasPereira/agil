(function () {
	'use strict';

	var agilAplicacao = angular.module('agilAplicacao', ['ngRoute']);

	agilAplicacao.config(['$routeProvider', function ($routeProvider) {

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
	
	agilAplicacao.run(['$rootScope', '$route', function($rootScope, $route) {
        $rootScope.$on('$locationChangeStart', function() {
        	var projetos = /^#\/projetos/;
        	var anoAtual = /^#\/projetos\/ano20[0-9][0-9]/;
        	var semestreAtual = /^#\/projetos\/ano20[0-9][0-9]\/semestre[1-2]/;
        	if (projetos.test(location.hash)) {
        		$rootScope.pastaBase = location.hash.replace(semestreAtual, "").replace(anoAtual, "").replace(projetos, "").replace(/$/, "/").replace(/^\//, "");
        	} else {
        		$rootScope.pastaBase = "";
        	}
        });
	}]);

}());
