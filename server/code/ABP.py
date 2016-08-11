
import requests
from bs4 import BeautifulSoup 
#Add your seed link here
import pdb
ROOT = 'http://www.anandabazar.com'
seeds = {
    'state':'http://www.anandabazar.com/state',
    }
from masterConfig import mconfig

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
 
 


