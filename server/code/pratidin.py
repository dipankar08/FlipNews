import hashlib
import requests
from bs4 import BeautifulSoup 
#Add your seed link here
import pdb
ROOT = 'http://zeenews.india.com'


def get_artical_info(url): #Working..
    try:
        print 'getting',url
        html = requests.get(url)
        if html.status_code != 200:
            print 'Error: Can;t retrive artical'
            return None
        #pdb.set_trace();
        soup = BeautifulSoup(html.text)
         
        story_container =  soup.find("article") 
     
        title = story_container.find('header').text
        date =  story_container.find("time", { "class" : "entry-date" }).text
        details = '\n'.join( [ p.text for p in story_container.find("div", { "class" : "entry-content" }).find_all('p')])
        #pdb.set_trace()
        images = [ m.get('src') for m in story_container.find_all('img') if m.get('src').endswith('jpg')]
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
        page_container = soup.find('div',{'class':'article-container'})
        #pdb.set_trace()
        #x = soup.find("div", { "class" : "lead-health" })
        #if x: links.append(x.find('a').get('href'))
 

        links += [ y.find('a').get('href') for y in page_container.find_all("article") if y.find('a')]
        
        global ROOT
        x1 = [ ROOT + l for l in links if l[0] == '/']
        x2 = [ l for l in links if l[0] != '/'] 
        return x1 + x2
         
    except Exception, e:
        print 'Error in (get_all_artical_links): ',url,str(e)
        return None
 


