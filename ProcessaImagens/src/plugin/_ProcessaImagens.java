package plugin;

import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import ij.IJ;
import ij.ImagePlus;
import ij.io.DirectoryChooser;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public class _ProcessaImagens implements PlugIn {

	public void run(String arg) {
		String caminhoPastaOrigem = new String();

		// caixa de diálogo para escolha da pasta com as imagens
		DirectoryChooser.setDefaultDirectory(System.getProperty("user.dir") + "/amostras/");
		ij.io.DirectoryChooser directoryOpen = new DirectoryChooser("Selecione a pasta com as imagens");
		caminhoPastaOrigem = directoryOpen.getDirectory();
		// cria uma pasta para receber os resultados das análises
		File pastaResultados = new File(caminhoPastaOrigem + new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date()));
		pastaResultados.mkdir();

		// Classe para manipulação de arquivos no disco
		File pastaOrigem = new File(caminhoPastaOrigem);
		// classe para filtrar os arquivos de imagem apenas .JPG e .JPEG na
		// funçao pastaOrigem.listFiles()
		FilenameFilter arquivosImagem = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(".jpg") || str.equals(".jpeg"))
						return true;
				}
				return false;
			}
		};
		// transfere para o vetor arquivosOrigem, todos os arquivos que
		// atenderem ao filtro arquivosImagem
		File[] arquivosOrigem = pastaOrigem.listFiles(arquivosImagem);

		// cria uma tabela do IJ para receber os resultados da análise
		ResultsTable tabelaResultados = new ResultsTable();
		// percorre o vetor arquivosImagem
		for (File arquivoAnalisar : arquivosOrigem) {
			if (arquivoAnalisar.isFile()) {
				// executa a funcção de análise das imagens em cada arquivo do
				// vetor arquivosImagem
				int resultado = analisarImagem(arquivoAnalisar, pastaResultados.getAbsolutePath() + "/");
				try {
					// armazena na tabela de resultados o nome do arquivo e o
					// valor retornado pela função de análise
					tabelaResultados.incrementCounter();
					tabelaResultados.addValue("Arquivo", arquivoAnalisar.getName());
					tabelaResultados.addValue("Resultado", resultado);
				} catch (Exception e) {
					IJ.showMessage(e.toString());
				}
			}
		}
		tabelaResultados.save(pastaResultados.getAbsolutePath() + "/resultados.csv");
		tabelaResultados.show("Resultados");
	}

	public int analisarImagem(File arquivoImagem, String caminhoImagemResultado) {
		int valorResultado = Integer.MAX_VALUE;
		ImagePlus imagemAnalise = IJ.openImage(arquivoImagem.getAbsolutePath());

		// Converte a imagem para escala de conza em 8 bits (0 a 255)
		ImageConverter ic = new ImageConverter(imagemAnalise);
		ic.convertToGray8();

		try {
			ImagePlus binarizada = new ImagePlus("", binarizarImagem(imagemAnalise.getProcessor()));
			ImageIO.write(binarizada.getBufferedImage(), "jpeg",
					new File(caminhoImagemResultado + "/" + arquivoImagem.getName() + "_passo01_binarizarImagem.jpg"));

			// tabela de resultados
			ResultsTable resultado = new ResultsTable();
			
			// Opções para a análise de particulas @ij.plugin.filter.ParticleAnalyzer
			int options = ParticleAnalyzer.SHOW_OUTLINES | ParticleAnalyzer.SHOW_ROI_MASKS
					| ParticleAnalyzer.DISPLAY_SUMMARY | ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES
					| ParticleAnalyzer.INCLUDE_HOLES;
			
			// Define a área mínima e máxima das particulas
			double minSize = 500, maxSize = Double.POSITIVE_INFINITY;

			// "Area"; [0]=AREA
			// "Mean gray value"; [1]=MEAN
			// "Standard deviation"; [2]=STD_DEV
			// "Modal gray value"; [3]=MODE
			// "Min & max gray value"; [4]=MIN_MAX
			// "Centroid"; [5]=CENTROID
			// "Center of mass"; [6]=CENTER_OF_MASS
			// "Perimeter"; [7]=PERIMETER
			// "Bounding rectangle"; [8]=RECT
			// "Fit ellipse"; [9]=ELLIPSE
			// "Shape descriptors"; [10]=SHAPE_DESCRIPTORS
			// "Feret's diameter"; [11]=FERET
			// "Integrated density"; [12]=INTEGRATED_DENSITY
			// "Median"; [13]=MEDIAN
			// "Skewness"; [14]=SKEWNESS
			// "Kurtosis"; [15]=KURTOSIS
			// "Area_fraction"; [16]=AREA_FRACTION
			// "Stack position"; [17]=STACK_POSITION
			
			// opções de medidas realizadas @ij.measure.Measurements
			int measurements = ij.measure.Measurements.AREA | ij.measure.Measurements.FERET;

			ParticleAnalyzer analisadorParticulas = new ParticleAnalyzer(options, measurements, resultado, minSize,
					maxSize);

			// Para editar os parâmetros manualmente
			// analisadorParticulas.showDialog();

			// Define se a imagem resultante será ocultada
			analisadorParticulas.setHideOutputImage(true);

			// Define a área de interesse na imagem (X, Y, Largura, Altura)
			// -> X começando em zero e aumentando à esquerda
			// -> Y começando em zero e aumentando à baixo
			binarizada.setRoi(10, 100, 620, 200);

			// Executa o processo de análise com as opções já definidas
			if (analisadorParticulas.analyze(binarizada)) {
				// TODO: definir fórmula e pesos para classificação
				// define o resultado com o número de áreas encontradas dentro
				// dos parâmentos informados
				valorResultado = resultado.getCounter();

				// grava as imagem e tabela de resultados
				ImageIO.write(analisadorParticulas.getOutputImage().getBufferedImage(), "jpeg", new File(
						caminhoImagemResultado + "/" + arquivoImagem.getName() + "_passo02_analisadorParticulas.jpg"));
				resultado.save(
						caminhoImagemResultado + "/" + arquivoImagem.getName() + "_passo02_analisadorParticulas.csv");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// retorna o valor calculado da análise
		return valorResultado;
	}

	public ImageProcessor binarizarImagem(ImageProcessor ip) {
		int width = ip.getWidth();
		Rectangle r = ip.getRoi();

		int offset, i;
		int[] ni = new int[256];
		float[] h = new float[256];
		float[] vetory = new float[256];

		int nt = ip.getHeight() * ip.getWidth();
		int hmax = 0, ymax = 0, T = 0;

		for (int y = 0; y < 255; y++) {
			ni[y] = 0;
		}
		for (int y = r.y; y < (r.y + r.height); y++) {
			offset = y * width;
			for (int x = r.x; x < (r.x + r.width); x++) {
				i = offset + x;
				try {
					ni[ip.getPixel(x, y)]++;
				} catch (Exception e) {
					IJ.showMessage("Pixel " + x + "," + y + " : " + ip.getPixel(x, y));
					return null;
				}
			}
		}

		// Calculo do vetor histograma
		for (int y = 0; y < 255; y++) {
			h[y] = ((float) ni[y]) / nt;
		}

		// calculo do maior valor do vetor h
		float max = 0;
		for (i = 0; i < 255; i++) {
			if (h[i] > max) {
				max = h[i];
				hmax = i;
			}
		}

		// calculo do novo vetor y
		for (i = 0; i < 255; i++) {
			vetory[i] = (i - hmax) * (i - hmax) * h[i];
		}

		// calculo do maior valor do vetor y
		max = 0;
		for (i = 0; i < 255; i++) {
			if (vetory[i] > max) {
				max = vetory[i];
				ymax = i;
			}
		}

		// calculo do limiar
		T = Math.round(hmax + ymax) / 2;
		for (int y = r.y; y < (r.y + r.height); y++) {
			offset = y * width;
			for (int x = r.x; x < (r.x + r.width); x++) {
				i = offset + x;
				if (ip.getPixel(x, y) <= T) {
					ip.putPixel(x, y, 0);
				} else {
					ip.putPixel(x, y, 255);
				}
			}
		}

		return ip;
	}

}
