(function () {
	'use strict';

	angular.module('agilAplicacao').controller('ProjetosControle', function ($scope, $http, $route, $location) {
		$scope.voltar = function () {
			var caminho =  $route.current.params.caminho;
			caminho = caminho.split("/");
			caminho.splice(caminho.length - 1, 1);
			caminho = caminho.join("/");
			$location.url("/projetos/" + caminho);
		}

		$scope.erroDeResposta = false;
		$scope.mostrarVoltar = ($route.current.params.caminho !== undefined);
		$scope.carregando = true;
		var requisicao = $http({
			method: 'GET',
			url: '/projetos',
			headers: {
				'Accept': 'application/json'
			}
		});
		requisicao.success(function (dados, estado, cabecalhos, configuracoes) {
			$scope.projetos = [];
			$scope.diretorios = {};
			var caminho =  $route.current.params.caminho;
			var indice;
			for (indice in dados) {
				var projeto = dados[indice];
				var sequenciaDeDiretorios;
				var nomeDoDiretorio;
				if (caminho == null || caminho === undefined) {
					sequenciaDeDiretorios = projeto.nome.split("/");
					if (sequenciaDeDiretorios.length === 1) {
						projeto.nomeDeExibicao = sequenciaDeDiretorios[0];
						$scope.projetos.push(projeto);
					} else if (sequenciaDeDiretorios.length > 1) {
						nomeDoDiretorio = sequenciaDeDiretorios[0];
						if ($scope.diretorios[nomeDoDiretorio] === undefined) {
							$scope.diretorios[nomeDoDiretorio] = { nome: nomeDoDiretorio, nomeDeExibicao: nomeDoDiretorio, ultimaAtualizacao: projeto.ultimaAtualizacao };
						} else if ($scope.diretorios[nomeDoDiretorio].ultimaAtualizacao < projeto.ultimaAtualizacao) {
							$scope.diretorios[nomeDoDiretorio].ultimaAtualizacao  = projeto.ultimaAtualizacao;
						}
					}
				} else {
					var regex = new RegExp("^" + caminho + "/");
					if (regex.test(projeto.nome)) {
						sequenciaDeDiretorios = projeto.nome.replace(regex, "").split("/");
						if (sequenciaDeDiretorios.length === 1) {
							projeto.nomeDeExibicao = sequenciaDeDiretorios[0];
							$scope.projetos.push(projeto);
						} else if (sequenciaDeDiretorios.length > 1) {
							var nomeDoDiretorio = sequenciaDeDiretorios[0];
							if ($scope.diretorios[nomeDoDiretorio] === undefined) {
								$scope.diretorios[nomeDoDiretorio] = { nome: caminho + "/" + nomeDoDiretorio, nomeDeExibicao: nomeDoDiretorio, ultimaAtualizacao: projeto.ultimaAtualizacao };
							} else if ($scope.diretorios[nomeDoDiretorio].ultimaAtualizacao < projeto.ultimaAtualizacao) {
								$scope.diretorios[nomeDoDiretorio].ultimaAtualizacao  = projeto.ultimaAtualizacao;
							}
						}
					}
				}
			}
			var diretoriosTemporarios = [];
			for (var chave in $scope.diretorios ) {
				if ($scope.diretorios .hasOwnProperty(chave)) {
					diretoriosTemporarios.push($scope.diretorios[chave]);
				}
			}
			$scope.diretorios = diretoriosTemporarios;
			$scope.breadcrumb = [];
			if (caminho !== null && caminho !== undefined) {
				var partesDoBreadcrumb = caminho.split("/");
				var uri = "/#/projetos";
				for (indice in partesDoBreadcrumb) {
					var itemDoBreadcrumb = partesDoBreadcrumb[indice];
					uri = uri + "/" + itemDoBreadcrumb;
					$scope.breadcrumb.push({
						nome: itemDoBreadcrumb,
						uri: uri
					});
				}
			}
			$scope.carregando = false;
		});
		requisicao.error(function (dados, estado, cabecalhos, configuracoes) {
			$scope.erroDeResposta = true;
			$scope.carregando = false;
		});
	});

}());
