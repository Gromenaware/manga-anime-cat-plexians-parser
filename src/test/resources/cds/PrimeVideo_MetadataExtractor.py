import requests, json, sys, time, argparse
from re import search, compile as recompile, DOTALL, sub

global uxLocale, gascEnabled, deviceID, deviceTypeID, USER_AGENT, prime_url

###################################################################################################

print()

parser = argparse.ArgumentParser()
parser.add_argument("--url", dest="prime_url", help="URL")
parser.add_argument("--uxlocale", dest="uxLocale", help="uxLocale", default="ca_ES")
args = parser.parse_args()

if not args.prime_url:
	prime_url = input("URL: ")
else:
	prime_url = str(args.prime_url)

uxLocale = args.uxLocale

###################################################################################################

gascEnabled = False
if 'primevideo.com' in prime_url:
	gascEnabled = True
	site_base_url = 'www.primevideo.com'
elif 'amazon.co.jp' in prime_url:
	site_base_url = 'www.amazon.co.jp'
elif 'amazon.co.uk' in prime_url:
	site_base_url = 'www.amazon.co.uk'
elif 'amazon.de' in prime_url:
	site_base_url = 'www.amazon.de'
elif 'amazon.com' in prime_url:
	site_base_url = 'www.amazon.com'
	
USER_AGENT = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36'
deviceID = '1234567890abcdef'
deviceTypeID = 'AFOQV1TK6EU6O'

headers_detailsPage = {
	'User-Agent': USER_AGENT,
	'accept-language': 'en'
}

###################################################################################################

def pause():
	programPause = input("Press the <ENTER> key to continue...")


def GetGTIasinAndAsinsList(self_json):
	for self in self_json['state']['self']:
		if self == 	self_json['state']['pageTitleId']:
			gti_asin = self_json['state']['self'][self]['gti']
			asins_list = self_json['state']['self'][self]['asins']
	return gti_asin, asins_list


def extract_json(content, name):
	json_dict = {}
	JSON_REGEX = r'<script type="text/template">(.*?)</script>'
	json_array = recompile(JSON_REGEX, DOTALL).findall(content)
	for json_str in json_array:
		final_json = json.loads(json_str)

		if 'compactGTI' in str(final_json):
			for body in final_json['props']['body']:
				if body['name'] == 'detail-vod':
					for prop in body['props']:
						if 'state' in body['props'][prop]:
							if 'compactGTI' in str(body['props'][prop]):
								if body['props'][prop]['state']['collections'] == {}:
									final_json_name = 'self'
									final_json_ = body['props'][prop]
									try:
										json_dict.update({final_json_name: final_json_})
									except Exception:
										continue
								else:
									final_json_name = 'self_collections'
									final_json_ = body['props'][prop]
									try:
										json_dict.update({final_json_name: final_json_})
									except Exception:
										continue

	return json_dict[name], json_dict


def Get_GTIAsin(prime_url):
	custom_headers = {
		'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
		'accept-language': 'en',
		'referer': prime_url,
		'user-agent': USER_AGENT
	}

	retry_counter = 0
	gti_asin = None
	while not gti_asin:
		if retry_counter > 20:
			print('Amazon is not working, retry it')
			break

		try:
			html_data = requests.get(prime_url, headers=custom_headers)
			self_json, html_json_all = extract_json(html_data.text, 'self')
			gti_asin, asins_list = GetGTIasinAndAsinsList(self_json)
			pageTitleId = self_json['state']['pageTitleId']
			entityType = self_json['state']['detail']['headerDetail'][pageTitleId]['entityType'].lower()
		except Exception as e:
			retry_counter = retry_counter + 1
			time.sleep(5)
	
	return gti_asin, entityType


def Get_detailsPageATF(gti_asin):
	params_detailsPageATF = {
		'itemId': gti_asin,
		'widgetScheme': 'lrc-detail-v3',
		'dynamicFeatures': 'CLIENT_DECORATION_ENABLE_DAAPI',
		'presentationScheme': 'living-room-react',
		'gascEnabled': gascEnabled,
		'uxLocale': uxLocale,
		'deviceTypeID': deviceTypeID,
		'deviceID': deviceID,
		'firmware': '1'
	}


	resp_detailsPageATF = requests.get(
		url='https://atv-ext.amazon.com/lrcedge/getDataByJavaTransform/v1/lr/detailsPage/detailsPageATF',
		params=params_detailsPageATF,
		headers=headers_detailsPage,
	).json()
	
	return resp_detailsPageATF['resource']


def Get_detailsPageBTF(gti_asin):
	params_detailsPageBTF = {
		'itemId': gti_asin,
		'widgetScheme': 'lrc-detail-btf-v2',
		'dynamicFeatures': 'CLIENT_DECORATION_ENABLE_DAAPI',
		'presentationScheme': 'living-room-react',
		'gascEnabled': gascEnabled,
		'uxLocale': uxLocale,
		'deviceTypeID': deviceTypeID,
		'deviceID': deviceID,
		'firmware': '1'
	}

	resp_detailsPageBTF = requests.get(
		url='https://atv-ext.amazon.com/lrcedge/getDataByJavaTransform/v1/lr/detailsPage/detailsPageBTF',
		params=params_detailsPageBTF,
		headers=headers_detailsPage,
	).json()
	
	return resp_detailsPageBTF['resource']
	
def Get_seasonsEpisodes(seasonGti):
	params_seasonsEpisodes = {		
		'seasonGti': seasonGti, 
		'pageSize': '999999', 
		'allSeasonsGtis': seasonGti, 
		'direction': 'left', 
		'presentationScheme': 'living-room-react', 
		'newMaturityRatings': 'true', 
		'caravanBTF': 'true', 
		#'roles': 'live-supported,startover-supported,linear-supported,supports-dai-timeshifting', 
		'gascEnabled': gascEnabled, 
		'uxLocale': uxLocale, 
		'deviceTypeID': deviceTypeID, 
		'deviceID': deviceID, 
		'firmware': '1'
	}

	resp_seasonsEpisodes = requests.get(
		url='https://atv-ext.amazon.com/lrcedge/getDataByJavaTransform/v1/lr/detailsPage/seasonsEpisodesNext',
		params=params_seasonsEpisodes,
		headers=headers_detailsPage,
	).json()
	
	return resp_seasonsEpisodes['resource']
	
def Get_playbackMetadata(item_id):
	params_playbackMetadata = {		
		'itemIds': item_id, 
		'resourceIds': 'CAROUSELS_METADATA', 
		'titleType': 'VOD', 
		'roles': 'live-supported,startover-supported,linear-supported,supports-dai-timeshifting', 
		'gascEnabled': gascEnabled, 
		'uxLocale': uxLocale, 
		'deviceTypeID': deviceTypeID, 
		'deviceID': deviceID, 
		'firmware': '1', 
	}

	resp_playbackMetadata = requests.get(
		url='https://atv-ext.amazon.com/lrcedge/getDataByJavaTransform/v1/lr/playback/playbackMetadata',
		params=params_playbackMetadata,
		headers=headers_detailsPage,
	).json()
	
	return resp_playbackMetadata['resource']

def Get_DetailPage(season_gti):
	custom_headers_api = {
		'User-Agent': USER_AGENT,
		'Accept': 'application/json'
	}
	
	DetailPage_json = requests.get(
		url=f"https://{site_base_url}/gp/video/api/getDetailPage",
		params={
			"titleID": season_gti,
			"isElcano": "1",
			"sections": "Btf"
		},
		headers=custom_headers_api
	).json()["widgets"]
	
	return DetailPage_json

def findPageInitial(search_title, search_gti_asin):
	params_findPageInitial = {
		'featureScheme': 'react-v4',
		'dynamicFeatures': 'AppleTVODEnabled,CLIENT_DECORATION_ENABLE_DAAPI,DigitalBundlePvcPvc,EpgContainerPagination,LinearTitles,LiveTab,SupportsImageTextLinkTextInStandardHero',
		'decorationScheme': 'lr-decoration-gen4-v4',
		'widgetScheme': 'lrc-search-v3.2',
		'isBrandingEnabled': 'false',
		'phrase': search_title,
		'pageType': 'search',
		'pageId': 'flex',
		'isFlexEnabled': 'true',
		'gascEnabled': gascEnabled,
		'uxLocale': uxLocale,
		'deviceTypeID': deviceTypeID,
		'deviceID': deviceID,
		'firmware': '1'
	}

	resp_findPageInitial = requests.get(
		url='https://atv-ext.amazon.com/lrcedge/getDataByJavaTransform/v1/lr/find/findPageInitial',
		params=params_findPageInitial,
		headers=headers_detailsPage,
	).json()
	
	find_item = None
	for container in resp_findPageInitial['resource']['containers']:
		for item in container['items']:
			if item['title'] == search_title and item['gti'] == search_gti_asin:
				find_item = item
				break
				
	return find_item

def CleanImageURL(image_url):
	if image_url:
		image_extension = image_url.split('.')[-1]
		if '._' in image_url:
			image_url = image_url.split('._')[0] + f'.{image_extension}'
	return image_url
	
###################################################################################################

gti_asin, entityType = Get_GTIAsin(prime_url)

gti_asins_list = []
if 'show' in entityType:	
	for season in sorted(Get_detailsPageBTF(gti_asin)['seasonsEpisodes']['seasons'], key=lambda k: int(k['seasonNumber'])):
		season_gti = season['gti']
		
		season_detailsPageATF = Get_detailsPageATF(season_gti)
		
		season_title = season_detailsPageATF['seasonTitle']
		season_synopsis = season_detailsPageATF['synopsis']
		season_images = season_detailsPageATF['images']
		
		find_season = findPageInitial(season_title, season_gti)
		if find_season:
			#season_images.update({'heroImage': find_season['heroImage']})
			season_images.update({'unbrandedCoverImage': find_season['unbrandedCoverImage']})
			season_images.update({'boxartImage': find_season['boxartImage']})
			#season_images.update({'coverImage': find_season['coverImage']})
			#season_images.update({'titleLogoImage': find_season['titleLogoImage']})
			#season_images.update({'providerLogoImage': find_season['providerLogoImage']})
			season_images.update({'poster2x3Image': find_season['poster2x3Image']})
		
		for image in season_images:
			season_images.update({image: CleanImageURL(season_images[image])})
		season_images_string = json.dumps(season_images, indent=4)
		
		print(f'Title: {season_title}\n')
		print(f'Synopsis: {season_synopsis}\n')
		print(f'Images: {season_images_string}\n\n')
		
		season_playbackMetadata = Get_playbackMetadata(season_gti)

		for episodesBySeasonIndex in season_playbackMetadata['vodCarouselsMetadata'][season_gti]['episodesBySeasonIndex']:
			for episode in season_playbackMetadata['vodCarouselsMetadata'][season_gti]['episodesBySeasonIndex'][episodesBySeasonIndex]:
				if 'bonus' not in episode['titleDetails']['entityType'].lower():
					episode_title = episode['titleDetails']['title']
					episode_number = str(episode['titleDetails']['currentEpisodeNumber']).zfill(2)
					episode_synopsis = episode['titleDetails']['synopsis']
					episode_images = episode['titleDetails']['images']
								
					for image in episode_images:
						episode_images.update({image: CleanImageURL(episode_images[image])})
					episode_images_string = json.dumps(episode_images, indent=4)
					
					print(f'Title episode {episode_number}: {episode_title}\n')
					print(f'Synopsis: {episode_synopsis}\n')
					print(f'Images: {episode_images_string}\n')	
		
		for episodesBySeasonIndex in season_playbackMetadata['vodCarouselsMetadata'][season_gti]['episodesBySeasonIndex']:
			for episode in season_playbackMetadata['vodCarouselsMetadata'][season_gti]['episodesBySeasonIndex'][episodesBySeasonIndex]:
				if 'bonus' in episode['titleDetails']['entityType'].lower():
					episode_title = episode['titleDetails']['title']
					episode_synopsis = episode['titleDetails']['synopsis']
					episode_images = episode['titleDetails']['images']
								
					for image in episode_images:
						episode_images.update({image: CleanImageURL(episode_images[image])})
					episode_images_string = json.dumps(episode_images, indent=4)
					
					print(f'Bonus title: {episode_title}\n')
					print(f'Synopsis: {episode_synopsis}\n')
					print(f'Images: {episode_images_string}\n')
		
		print('\n\n\n')
				
else:
	detailsPageATF = Get_detailsPageATF(gti_asin)
	
	movie_title = detailsPageATF['title']
	movie_synopsis = detailsPageATF['synopsis']
	movie_images = detailsPageATF['images']
	
	find_movie = findPageInitial(movie_title, gti_asin)
	if find_movie:
		#movie_images.update({'heroImage': find_movie['heroImage']})
		movie_images.update({'unbrandedCoverImage': find_movie['unbrandedCoverImage']})
		movie_images.update({'boxartImage': find_movie['boxartImage']})
		#movie_images.update({'coverImage': find_movie['coverImage']})
		#movie_images.update({'titleLogoImage': find_movie['titleLogoImage']})
		#movie_images.update({'providerLogoImage': find_movie['providerLogoImage']})
		movie_images.update({'poster2x3Image': find_movie['poster2x3Image']})
	
	for image in movie_images:
		movie_images.update({image: CleanImageURL(movie_images[image])})
	movie_images_string = json.dumps(movie_images, indent=4)
	
	print(f'Title: {movie_title}\n')
	print(f'Synopsis: {movie_synopsis}\n')
	print(f'Images: {movie_images_string}\n')

print()

pause()
