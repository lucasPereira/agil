(function () {
	'use strict';

	angular.module('projetosAplicacao').controller('ProjetosControle', function ($scope, $http) {
		$scope.erroDeResposta = false;
		$scope.carregando = true;
		var requisicao = $http({
			method: 'GET',
			url: '/projetos',
			headers: {
				'Accept': 'application/json'
			}
		});
		requisicao.success(function (dados, estado, cabecalhos, configuracoes) {
			$scope.projetos = dados;
			$scope.carregando = false;
		});
		requisicao.error(function (dados, estado, cabecalhos, configuracoes) {
			$scope.erroDeResposta = true;
			$scope.carregando = false;
		});
	});

}());
