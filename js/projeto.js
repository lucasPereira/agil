(function () {
	'use strict';

	angular.module('projetosAplicacao').controller('ProjetoControle', function ($scope, $location, $http, $route) {

		function inicializarVariaveisProjeto() {
			$scope.projetoSucesso = false;
			$scope.projetoNaoEncontrado = false;
			$scope.projetoErroDeResposta = false;
		}

		function inicializarVariaveisArquivos() {
			$scope.arquivosSucesso = false;
			$scope.arquivosNaoEncontrado = false;
			$scope.arquivosErroDeResposta = false;
		}

		function inicializarVariaveisImportacao() {
			$scope.importacaoTotal = 0;
			$scope.importacaoCarregado = 0;
			$scope.importacaoExtensaoPermitida = '.zip';
			$scope.importacaoTamanhoMaximoPermitido = '50 MB';
			$scope.importacaoSucesso = false;
			$scope.importacaoInformacaoSelecionarArquivo = false;
			$scope.importacaoErroExtensao = false;
			$scope.importacaoErroTamanhoMaximo = false;
			$scope.importacaoErroEnvioDoArquivo = false;
			$scope.importacaoErroCancelamento = false;
			$scope.importacaoErroEstouroDeTempo = false;
			$scope.importacaoArquivoInvalido = false;
			$scope.importacaoErroDeResposta = false;
		}

		function inicializarVariaveisClassePrincipal() {
			$scope.classePrincipalSelecionada = false;
			$scope.classePrincipalSucesso = false;
			$scope.classePrincipalErroDeCompilacao = false;
			$scope.classePrincipalErroDeResposta = false;
		}

		$scope.obterUriDeExecucao = function () {
			return '/projeto/' + $route.current.params.identificador + "/execucao";
		}

		$scope.obterUriDeExportacao = function () {
			return '/projeto/' + $route.current.params.identificador + "/arquivos.zip";
		}

		$scope.obterUriDoArquivoAtual = function () {
			return '/projeto/' + $route.current.params.identificador + "/arquivo/" + $route.current.params.caminho;
		}

		function buscarProjeto() {
			$scope.carregando = true;
			var requisicao = $http({method: 'GET', url: '/projeto/' + $route.current.params.identificador, headers: {'Accept': 'application/json'}});
			requisicao.success(receberProjetoComSucesso);
			requisicao.error(receberProjetoComErro);
		}

		function buscarArquivosDoProjeto() {
			$scope.carregando = true;
			var requisicao = $http({method: 'GET', url: '/projeto/' + $route.current.params.identificador + '/arquivos', headers: {'Accept': 'application/json'}});
			requisicao.success(receberArquivosDoProjetoComSucesso);
			requisicao.error(receberArquivosDoProjetoComErro);
		}

		$scope.salvarClassePrincipal = function () {
			$scope.carregando = true;
			if ($scope.classePrincipalSelecionada) {
				var requisicao = $http({method: 'PUT', url: '/projeto/' + $route.current.params.identificador + '/classePrincipal', data: $scope.classePrincipalSelecionada, headers: {'Content-Type': 'application/json'}});
				requisicao.success(fixarClassePrincipalComSucesso);
				requisicao.error(fixarClassePrincipalComErro);
			}
		}

		function receberProjetoComSucesso(dados, estado, cabecalhos, configuracoes) {
			$scope.projeto = dados;
			if ($scope.projeto.classePrincipal === undefined) {
				$scope.projeto.classePrincipal = null;
			}
			$scope.uriCaminho = $route.current.params.identificador + "/" + $route.current.params.caminho;
			$scope.projetoSucesso = true;
			document.querySelector('#selecaoDeProjeto').addEventListener('change', importarSelecionarArquivo, false);
			buscarArquivosDoProjeto();
			$scope.carregando = false;
		}

		function receberProjetoComErro(dados, estado, cabecalhos, configuracoes) {
			if (estado === 404) {
				$scope.projetoNaoEncontrado = true;
			} else {
				$scope.projetoErroDeResposta = true;
			}
			$scope.carregando = false;
		}

		function fixarClassePrincipalComSucesso(dados, estado) {
			$scope.classePrincipalSucesso = true;
			inicializarVariaveisProjeto();
			inicializarVariaveisArquivos();
			inicializarVariaveisImportacao();
			buscarProjeto();
			$scope.carregando = false;
		}

		function fixarClassePrincipalComErro(dados, estado) {
			console.log(dados);
			if (estado === 400) {
				$scope.classePrincipalErroDeCompilacao = true;
			} else {
				$scope.classePrincipalErroDeResposta = true;
			}
			$scope.carregando = false;
		}

		function receberArquivosDoProjetoComSucesso(dados, estado, cabecalhos, configuracoes) {
			$scope.arquivoRaiz = dados
			$scope.arquivoAtual = $scope.arquivoRaiz;
			$scope.pilha = [$scope.arquivoAtual];
			$scope.arquivosSucesso = true;
			$scope.classes = encontrarClassesJava();
			var sequencia = encontrarCaminho($scope.arquivoAtual, $scope.uriCaminho);
			for (var indice in sequencia) {
				$scope.entrar(sequencia[indice]);
			}
			$scope.carregando = false;
		}

		function receberArquivosDoProjetoComErro(dados, estado, cabecalhos, configuracoes) {
			if (estado === 404) {
				$scope.arquivosNaoEncontrado = true;
			} else {
				$scope.arquivosErroDeResposta = true;
			}
			$scope.carregando = false;
		}

		$scope.voltar = function () {
			$scope.arquivoAtual = $scope.pilha.pop();
		}

		$scope.entrar = function (arquivo) {
			if (arquivo !== $scope.arquivoAtual) {
				$scope.pilha.push($scope.arquivoAtual);
				$scope.arquivoAtual = arquivo;
			}
		}

		$scope.irParaInicio = function (arquivo) {
			$scope.arquivoAtual = $scope.arquivoRaiz;
			$scope.pilha = [$scope.arquivoAtual];
		}

		$scope.estaNoInicio = function () {
			return $scope.pilha.length === 1
		}

		function encontrarClassesJava() {
			var src;
			for (var indice in $scope.arquivoRaiz.filhos) {
				var filho = $scope.arquivoRaiz.filhos[indice];
				if (filho.nome === "src") {
					src = filho;
				}
			}
			var classes = [];
			if (src !== null) {
				adicionarClasses(src, classes);
			}
			return classes;
		}

		function adicionarClasses(nodo, classes) {
			for (var indice in nodo.filhos) {
				var filho = nodo.filhos[indice];
				if (filho.diretorio) {
					adicionarClasses(filho, classes);
				} else {
					var classe = {
						nome: filho.caminho.replace(/\//g, '.').replace($scope.arquivoRaiz.nome + '.src.', '').replace(/\.java$/, ''),
						caminho: filho.caminho
					}
					if ($scope.projeto.classePrincipal === classe.nome) {
						$scope.classePrincipalSelecionada = classe;
					}
					classes.push(classe);
				}
			}
		}

		function encontrarCaminho(nodo, caminho) {
			if (nodo.caminho === caminho || nodo.caminho + "/" === caminho) {
				return [nodo];
			}
			if (nodo.filhos) {
				for (var indice in nodo.filhos) {
					var filho = nodo.filhos[indice];
					var caminhoDoFilho = encontrarCaminho(filho, caminho);
					if (caminhoDoFilho !== null) {
						var sequencia = [nodo];
						sequencia.push.apply(sequencia, caminhoDoFilho);
						return sequencia;
					}
				}
			}
			return null;
		}
		$scope.abrirImportacaoDeProjeto = function () {
			inicializarVariaveisImportacao();
			document.querySelector("#selecaoDeProjeto").click();
		}

		function importarSelecionarArquivo(evento) {
			var descricoesDosArquivos = evento.target.files;
			if (descricoesDosArquivos.length > 0) {
				var descricaoDoArquivo = descricoesDosArquivos[0];
				console.log(descricaoDoArquivo.name);
				console.log(descricaoDoArquivo.type);
				console.log(descricaoDoArquivo.size);
				importarVerificarTamanho(descricaoDoArquivo);
			} else {
				$scope.importacaoInformacaoSelecionarArquivo = true;
			}
			$scope.$apply();
			document.querySelector('#selecaoDeProjeto').value = null;
		}

		function importarVerificarExtensao(descricaoDoArquivo) {
			var extensaoPossivel = 'application/zip';
			if (descricaoDoArquivo.type === extensaoPossivel) {
				importarVerificarTamanho(descricaoDoArquivo);
			} else {
				$scope.importacaoErroExtensao = true;
			}
		}

		function importarVerificarTamanho(descricaoDoArquivo) {
			var tamanhoPossivel = 1024 * 1024 * 50;
			if (descricaoDoArquivo.size <= tamanhoPossivel) {
				importar(descricaoDoArquivo);
			} else {
				$scope.importacaoErroTamanhoMaximo = true;
			}
		}

		function importar(descricaoDoArquivo) {
			var dadosDoFormulario = new FormData();
			dadosDoFormulario.append('arquivos', descricaoDoArquivo);
			var requisicao = new XMLHttpRequest();
			requisicao.addEventListener('error', importacaoErro, false);
			requisicao.addEventListener('abort', importacaoCancelamento, false);
			requisicao.addEventListener('timeout', importacaoEstouroDeTempo, false);
			requisicao.addEventListener('load', importacaoSucesso, false);
			requisicao.upload.addEventListener('progress', importacaoProgresso, false);
			requisicao.open('PUT', $location.path() + '/arquivos', true);
			requisicao.send(dadosDoFormulario);
		}

		function importacaoErro() {
			$scope.importacaoErroEnvioDoArquivo = true;
			$scope.$apply();
		}

		function importacaoCancelamento() {
			$scope.importacaoErroCancelamento = true;
			$scope.$apply();
		}

		function importacaoEstouroDeTempo() {
			$scope.importacaoErroEstouroDeTempo= true;
			$scope.$apply();
		}

		function importacaoSucesso(progresso) {
			if (progresso.target.status === 200) {
				$scope.importacaoSucesso = true;
				inicializarVariaveisProjeto();
				inicializarVariaveisArquivos();
				inicializarVariaveisClassePrincipal();
				buscarProjeto();
			} else if (progresso.target.status ) {
				$scope.importacaoArquivoInvalido = true;
			} else {
				$scope.importacaoErroDeResposta = true;
			}
			$scope.$apply();
		}

		function importacaoProgresso(evento) {
			$scope.importacaoCarregado = evento.loaded;
			$scope.importacaoTotal = evento.total;
			$scope.$apply();
		}

		function atualizar() {
			inicializarVariaveisProjeto();
			inicializarVariaveisArquivos();
			inicializarVariaveisImportacao();
			inicializarVariaveisClassePrincipal();
			buscarProjeto();
		}

		atualizar();

	});

}());
