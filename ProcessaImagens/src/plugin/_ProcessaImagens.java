package plugin;

import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.io.*;
import ij.measure.ResultsTable;

import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

import ij.plugin.*;

public class _ProcessaImagens implements PlugIn {

	public void run(String arg) {
		String caminhoPastaOrigem = new String();
		String caminhoArquivoResultados = new String();

		// caixa de diálogo para escolha da pasta com as imagens
		DirectoryChooser.setDefaultDirectory(System.getProperty("user.dir") + "/amostras/");
		ij.io.DirectoryChooser directoryOpen = new DirectoryChooser("Selecione a pasta com as imagens");
		caminhoPastaOrigem = directoryOpen.getDirectory();
		caminhoArquivoResultados = caminhoPastaOrigem + new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date());

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

		// File pastaResultados = new File(caminhoPastaResultados);
		// pastaResultados.mkdir();

		// cria uma tabela do IJ para receber os resultados da análise
		ResultsTable resultstable = new ResultsTable();
		// percorre o vetor arquivosImagem
		for (int i = 0; i < arquivosOrigem.length; i++) {
			if (arquivosOrigem[i].isFile()) {
				// executa a funcção de análise das imagens em cada arquivo do
				// vetor arquivosImagem
				int resultado = analisarImagem(arquivosOrigem[i].getAbsolutePath(), caminhoArquivoResultados);
				// armazena na tabela de resultados o nome do arquivo e o valor
				// retornado pela função de análise
				resultstable.setValue("Arquivo", i, arquivosOrigem[i].getName());
				resultstable.setValue("Resultado", i, resultado);
			}
		}

		resultstable.save(caminhoArquivoResultados + ".xls");
		resultstable.show("Resultados");
	}

	public int analisarImagem(String caminhoArquivo, String caminhoImagemResultado) {
		ImagePlus imagem = IJ.openImage(caminhoArquivo);
		// ImagePlus binarizada = new ImagePlus("",
		// binarizarImagem(imagem.getProcessor()));
		// binarizada.show();
		binarizarImagem(imagem.getProcessor());
		//imagem.show();
		return imagem.hashCode();
	}

	public void binarizarImagem(ImageProcessor ip) {
		int width = ip.getWidth();
		Rectangle r = ip.getRoi();

		int offset, i;
		int[] ni = new int[256];
		float[] h = new float[256];
		float[] vetory = new float[256];

		int nt = ip.getHeight() * ip.getWidth();
		int hmax = 0, ymax = 0, T = 0;
		GenericDialog gd = new GenericDialog("Results");

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
					return;
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
			;
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
			;
		}

		// calculo do limiar
		T = Math.round(hmax + ymax) / 2;

		// mostra resultados
		gd.addNumericField("Threshold value: ", T, 0);
		gd.showDialog();

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

		// return ip;
	}

}
