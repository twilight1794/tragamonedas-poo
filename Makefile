.PHONY: build clean_java clean_java_all clean_tex clean_tex all clean clean_min doc publish run tex

all: build doc tex

build:
	@echo "Compilar..."
	@cd src && javac xyz/campanita/poofinal/cliente/Cliente.java
	@rm -f programa.jar
	@cd src && jar cfe ../programa.jar xyz.campanita.poofinal.cliente.Cliente xyz/campanita/poofinal/*/*.class

clean_java:
	@echo "Limpiando en Java..."
	@cd src && find . -name '*~' -delete

clean_java_all: clean_java
	@find . -name '*.class' -delete
	@rm -f programa.jar

clean_tex:
	@echo "Limpiando en docs..."
	@rm -rf docs/_minted*/
	@rm -f docs/*.aux
	@rm -f docs/*.log
	@rm -f docs/*.out
	@rm -f docs/*.toc
	@rm -f texput.log
	@cd docs && find . -name '*~' -delete

clean_tex_all: clean_tex
	@rm -f docs/*.pdf

clean: clean_java_all clean_tex_all
	@rm -f docs/*.png

clean_min: clean_java clean_tex

doc:
	@echo "Documentando..."
	@mkdir -p docs
	@javadoc -d docs/javadoc src/xyz/campanita/poofinal/*/*.java

publish:
	@echo "Empaquetando..."
	@rm -f proyecto.tar.gz
	@tar -zcvf proyecto.tar.gz --exclude="`find . -name '*~'`" --exclude="docs/_minted*" --exclude "docs/*.aux" --exclude="docs/*.log" --exclude="docs/*.out" --exclude="docs/*.toc" --exclude="texput.log" --exclude="proyecto.tar.gz" ./*
run:
	@java -jar programa.jar

tex:
	@cd docs && if test -e "informe.tex" ; then pdflatex -shell-escape informe.tex ; fi
	@cd docs && if test -e "manual.tex" ; then lualatex -shell-escape manual.tex ; fi
	@java -jar plantuml-1.2022.1.jar -tpng *.puml
