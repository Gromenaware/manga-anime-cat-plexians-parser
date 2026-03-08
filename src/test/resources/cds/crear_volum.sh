#!/bin/bash

# 1. Definim la carpeta arrel.
# ${1:-.} significa: "Agafa la ruta que l'usuari escrigui. Si no n'escriu cap, fes servir la carpeta actual (.)"
CARPETA_ARREL="${1:-.}"

echo "🚀 Iniciant el procés a la carpeta: $CARPETA_ARREL"

# 2. Entrem a la carpeta arrel. Si la ruta no existeix, mostrem un error i aturem l'script.
cd "$CARPETA_ARREL" || { echo "❌ Error: No s'ha trobat la carpeta '$CARPETA_ARREL'."; exit 1; }

# 3. Extraiem el nom de la carpeta actual per donar nom al volum automàticament
NOM_CARPETA=$(basename "$PWD")
NOM_VOLUM="${NOM_CARPETA}_complet.cbr"

echo "📦 Recopilant totes les imatges per crear: $NOM_VOLUM..."

# 4. Comprovem si existeixen fitxers .jpg a les subcarpetes abans de comprimir
if ls episodi_*/*.jpg >/dev/null 2>&1; then

    # Creem l'arxiu .cbr (format zip) incloent només els fitxers .jpg
    # L'opció -q fa que el procés sigui silenciós
    zip -q "$NOM_VOLUM" episodi_*/*.jpg

    # Comprovem si la comanda zip ha funcionat correctament
    if [ $? -eq 0 ]; then
        echo "✅ Volum creat amb èxit! S'ha guardat com a '$NOM_VOLUM'."
    else
        echo "❌ Hi ha hagut un error en crear el volum."
    fi
else
    echo "⚠️ No s'han trobat imatges .jpg a les carpetes d'episodis."
fi

echo "🎉 Procés completat!"
