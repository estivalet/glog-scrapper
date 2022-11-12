import json
from lxml import html
import requests
import time

current_page = 1
url = 'https://gamesdb.launchbox-app.com/platforms/games/8'
page = requests.get(url)
tree = html.fromstring(page.content)

str_content = str(page.content)
i = str_content.find('totalPages: ')
if(i>0):
    j = str_content.find(',', i)
    total_pages = int(str_content[i+12:j])
    url += "%7C"
else:
    total_pages = 1

while current_page <= total_pages:

    for item in tree.xpath('//a[@class="list-item"]'):
        game_href = item.get('href')

        try:
            page = requests.get('https://gamesdb.launchbox-app.com' + str(game_href))
        except:
            time.sleep(5)
            page = requests.get('https://gamesdb.launchbox-app.com' + str(game_href))

        tree = html.fromstring(page.content)

        name = tree.xpath("//td[.='Name']/following::td[1]/span/text()")
        overview = tree.xpath("//td[.='Overview']/following::td[1]/div/text()")
        platform = tree.xpath("//td[.='Platform']/following::td[1]/span/text()")
        release_date = tree.xpath("//td[.='Release Date']/following::td[1]/span/text()")
        game_type = tree.xpath("//td[.='Game Type']/following::td[1]/span/text()")
        esrb = tree.xpath("//td[.='ESRB']/following::td[1]/span/text()")
        developers = tree.xpath("//td[.='Developers']/following::td[1]/span/a/text()")
        publishers = tree.xpath("//td[.='Publishers']/following::td[1]/span/a/text()")
        genres = tree.xpath("//td[.='Genres']/following::td[1]/span/a/text()")
        max_players = tree.xpath("//td[.='Max Players']/following::td[1]/span/text()")
        cooperative = tree.xpath("//td[.='Cooperative']/following::td[1]/span/text()")
        rating = tree.xpath("//td[.='Rating']/following::td[1]/span/text()")
        wikipedia = tree.xpath("//td[.='Wikipedia']/following::td[1]/span/text()")
        video_link = tree.xpath("//td[.='Video Link']/following::td[1]/span/a/text()")

        print(name)	
        print(overview)	
        print(platform)	
        print(release_date)	
        print(game_type)	
        print(esrb)	
        print(developers)	
        print(publishers)	
        print(genres)	
        print(max_players)	
        print(cooperative)	
        print(rating)	
        print(wikipedia)	
        print(video_link)	

        data = {}
        data['name'] = name[0].strip()
        data['overview'] = overview[0].replace('\r\n','<br>') if overview else ''
        data['platform'] = platform[0] if platform else ''
        data['release_date'] = release_date[0] if release_date else ''
        data['game_type'] = game_type[0] if game_type else ''
        data['esrb'] = esrb[0] if esrb else ''
        data['developers'] = developers[0] if developers else ''
        data['publishers'] = publishers[0] if publishers else ''
        data['genres'] = genres[0] if genres else ''
        data['max_players'] = max_players[0] if max_players else ''
        data['cooperative'] = cooperative[0] if cooperative else ''
        data['rating'] = rating[0] if rating else ''
        data['wikipedia'] = wikipedia[0] if wikipedia else ''
        data['video_link'] = video_link[0] if video_link else ''
        data['launchbox_gamesdb_link'] = 'https://gamesdb.launchbox-app.com' + str(game_href)

        with open('c:/temp/atari7800/' + name[0].replace(":"," -").replace("*"," ").replace("?", " ").replace("/"," ").replace("\\","").replace("\"","'").strip() + '.json', 'w') as outfile:
            json.dump(data, outfile)
        time.sleep(1)
    current_page += 1
    if(total_pages > 1):
        page = requests.get(url + str(current_page))
        tree = html.fromstring(page.content)
