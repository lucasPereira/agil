(function () {
	'use strict';

	angular.module('projetosAplicacao').controller('ProjetosControle', function ($scope, $http) {
		$scope.erroDeResposta = false;
		var requisicao = $http({
			method: 'GET',
			url: '/projetos',
			headers: {
				'Accept': 'application/json'
			}
		});
		requisicao.success(function (dados, estado, cabecalhos, configuracoes) {
			$scope.projetos = dados;
		});
		requisicao.error(function (dados, estado, cabecalhos, configuracoes) {
			$scope.erroDeResposta = true;
		});
	});

}());
