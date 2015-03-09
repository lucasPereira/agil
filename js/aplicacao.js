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
			controller: 'ProjetoNovoControle'
		});

	}]);

}());
