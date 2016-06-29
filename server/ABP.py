import hashlib
import requests
from bs4 import BeautifulSoup 
#Add your seed link here
import pdb
ROOT = 'http://www.anandabazar.com'
seeds = {
    'state':'http://www.anandabazar.com/state',
    }
 
def get_artical_info(url):
    try:
        print 'getting',url
        html = requests.get(url)
        if html.status_code != 200:
            print 'Error: Can;t retrive artical'
            return None
        #pdb.set_trace();
        soup = BeautifulSoup(html.text)
         
        story_container =  soup.find("div", { "id" : "story_container" }) 
     
        title = story_container.find('h1').text
        date =  story_container.find("div", { "class" : "publish_date" }).text
        details =  story_container.find("div", { "class" : "articleBody" }).text
        #pdb.set_trace()
        images = [ m.get('src') for m in story_container.find_all('img') if m.get('src').startswith('http://images1.') and m.get('src').endswith('jpg')]
        head_image = images[0] if len(images) > 0 else None
        return {'title':title,'date':date,'details':details,'images':images,'head_image':head_image}
    except Exception, e:
        print 'Error in (get_artical_info): ',url,str(e)
        return None
      
 
def get_all_artical_links(url):
    try:
        print 'getting',url
        html = requests.get(url)
        if html.status_code != 200:
            print 'Error: Can;t retrive artical'
            return None
        #pdb.set_trace();
        soup = BeautifulSoup(html.text)
         
        links =[]
 
        x = soup.find("div", { "class" : "leadstory-section" })
        if x: links.append(x.find('a').get('href'))
 
        x = soup.find("div", { "class" : "sectionstory-inside" })
        if x:
            links += list (set([ y.get('href') for y in x.find_all('a') if 'javascript' not in y.get('href')]))
        global ROOT
        return [ ROOT + l for l in links if l[0] == '/']
                  
         
    except Exception, e:
        print 'Error in Group',url,str(e)
        return None
 
 
#All Interface must implemnets this...
import random,string
def get_all_data_for_a_seed(seed):
    try:
        links = get_all_artical_links(seed['url'])
        if not links:
            return None
        res = []
        #pdb.set_trace()
        for l in links:
            ares = get_artical_info(l)
            if not ares:
                continue
            ares['url'] = l
            ares['source'] = 'Anadabazar'
            ares['categories']= seed['categories']
            ares['tags']= seed['categories']
            ares['rand_id']= hashlib.sha224(ares['url']).hexdigest()#''.join(random.SystemRandom().choice(string.ascii_uppercase + string.digits) for _ in range(20)) 
            res.append(ares)
        return res
    except Exception, e:
        print 'Error(get_all_data_for_a_seed)',seed,str(e)
        return None
 
         
#test Public API

