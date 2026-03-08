#!/bin/bash

# 1. Definim la carpeta arrel.
# ${1:-.} significa: "Agafa la ruta que l'usuari escrigui. Si no n'escriu cap, fes servir la carpeta actual (.)"
CARPETA_ARREL="${1:-.}"

echo "🚀 Iniciant el procés a la carpeta: $CARPETA_ARREL"

# 2. Entrem a la carpeta arrel. Si la ruta no existeix, mostrem un error i aturem l'script.
cd "$CARPETA_ARREL" || { echo "❌ Error: No s'ha trobat la carpeta '$CARPETA_ARREL'."; exit 1; }

# 3. Recorrem totes les carpetes que comencin per "episodi_"
for directori in episodi_*/; do

    # Comprovem que realment sigui un directori (evita errors si no hi ha cap carpeta que coincideixi)
    [ -d "$directori" ] || continue

    # Extraiem el nom net de la carpeta (sense la barra final)
    nom_carpeta=$(basename "$directori")

    echo "📂 Processant: $nom_carpeta"

    # Entrem al directori de l'episodi. Si falla, saltem al següent
    cd "$directori" || continue

    # Comprovem si existeixen fitxers .jpg per evitar errors
    if ls *.jpg >/dev/null 2>&1; then
        # Creem l'arxiu .cbr (format zip) només amb els fitxers .jpg
        # L'opció -q fa que el procés sigui silenciós i no ompli la pantalla
        zip -q "${nom_carpeta}.cbr" *.jpg

        echo "✅ Creat: ${nom_carpeta}.cbr només amb imatges."
    else
        echo "⚠️ No s'han trobat imatges .jpg a $nom_carpeta."
    fi

    # Tornem al directori pare (la carpeta arrel) per continuar el bucle
    cd ..
done

echo "🎉 Procés completat amb èxit!"