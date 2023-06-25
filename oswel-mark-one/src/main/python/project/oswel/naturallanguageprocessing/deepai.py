# Example directly sending a text string:

import requests
r = requests.post(
    "https://api.deepai.org/api/text-generator",
    data={
        'text': 'It is higher than 30 degrees celsius outside.',
    },
    headers={'api-key': 'quickstart-QUdJIGlzIGNvbWluZy4uLi4K'}
)
print(r.json())