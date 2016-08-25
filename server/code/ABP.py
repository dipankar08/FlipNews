#Moving to Spider

import requests
from bs4 import BeautifulSoup 
import Spider
#Add your seed link here
import pdb
ROOT = 'http://www.anandabazar.com'

def get_artical_info(url):
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
        
        #define the prory to retrive from soup.
        info2 = {
                    'title':['h1','text'],
                    'date':['.publish_date','text'],
                    'details':['.articleBody','text'],
                    'images':['img','src'],
                    'video':['iframe','src']
                }
                
        #Get the info from soup.
        x = Spider.getAttrListForXPath(soup,'#story_container', None,info2)
        if x == None:
            return None
        info = x[0]
        
        #Make some modification on info.
        if info.get('images') != None:
            new =[]
            for f in info['images']:
                if f.startswith('/'):
                    new.append(ROOT+ f)
                else:
                    new.append(f)
            info['images'] = new
            info['head_image'] = new[0]
        
        return info
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
        
        
def test():
    i = get_artical_info('http://www.anandabazar.com/entertainment/i-didn-t-know-i-am-so-seductive-says-swastika-mukherjee-dgtl-1.459800#')
    pdb.set_trace()
    
    
#test();
 
 


