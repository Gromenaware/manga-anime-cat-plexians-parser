import requests

# Define the headers
headers = {
    'Content-Type': 'application/json',
    'Host': 'odiloid.odilo.us'
}

# Make the GET request
url = 'https://odiloid.odilo.us/ClientId/byApp?app=ODILO_APP'
bibliolist_response = requests.get(url, headers=headers).json()

# Print the response
# Print only the 'url' value for each dictionary
for item in bibliolist_response:
    print(item['url'])