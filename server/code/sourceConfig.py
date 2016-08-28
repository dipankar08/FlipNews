import ABP
import zeenews
import pratidin
import bartaman
import eisamay
import oneindia
import healthbarta
config = {
    'bartaman': {
        'name':'bartaman',
        'handaler_info':bartaman.get_artical_info,
        'handaler_list':bartaman.get_all_artical_links,
        'seeds':[ 
            {'categories':'calcutta','url':'http://zeenews.india.com/bengali/kolkata.html'},
            {'categories':'state','url':'http://zeenews.india.com/bengali/zila.html'},
            {'categories':'national','url':'http://zeenews.india.com/bengali/nation.html'},
            {'categories':'international','url':'http://zeenews.india.com/bengali/world.html'},
            {'categories':'lifestyle','url':'http://zeenews.india.com/bengali/lifestyle'},
        ],       
    
    },
    'anandabazar': {
        'name':'anandabazar',
        'handaler_info':ABP.get_artical_info,
        'handaler_list':ABP.get_all_artical_links,
        'seeds':[
            {'categories':'firstpage','url':'http://www.anandabazar.com','parser':ABP.first_page_parser,'max_count':100},
            {'categories':'science','url':'http://www.anandabazar.com/others/science'},
            {'categories':'calcutta','url':'http://www.anandabazar.com/calcutta'},
            {'categories':'state','url':'http://www.anandabazar.com/state'},
            {'categories':'national','url':'http://www.anandabazar.com/national'},
            {'categories':'international','url':'http://www.anandabazar.com/international'},
            {'categories':'sports','url':'http://www.anandabazar.com/khela'},
            {'categories':'entertainment','url':'http://www.anandabazar.com/entertainment'},
            {'categories':'business','url':'http://www.anandabazar.com/business'},
            {'categories':'lifestyle','url':'http://www.anandabazar.com/lifestyle'},
            {'categories':'girls','url':'http://www.anandabazar.com/manabi'},           
            
        ],       
    
    },  
    'zeenews': {
        'name':'zeenews',
        'handaler_info':zeenews.get_artical_info,
        'handaler_list':zeenews.get_all_artical_links,
        'seeds':[ 
            {'categories':'calcutta','url':'http://zeenews.india.com/bengali/kolkata.html'},
            {'categories':'state','url':'http://zeenews.india.com/bengali/zila.html'},
            {'categories':'national','url':'http://zeenews.india.com/bengali/nation.html'},
            {'categories':'international','url':'http://zeenews.india.com/bengali/world.html'},
            {'categories':'lifestyle','url':'http://zeenews.india.com/bengali/lifestyle'},
        ],       
    
    },
    'sangbadpratidin': {
        'name':'sangbadpratidin',
        'handaler_info':pratidin.get_artical_info,
        'handaler_list':pratidin.get_all_artical_links,
        'seeds':[
            {'categories':'calcutta','url':'http://sangbadpratidin.in/category/kolkata/'},
            {'categories':'state','url':'http://sangbadpratidin.in/category/state/'},
            {'categories':'national','url':'http://sangbadpratidin.in/category/country/'},
            {'categories':'international','url':'http://sangbadpratidin.in/category/international/'},
            {'categories':'entertainment','url':'http://sangbadpratidin.in/category/film-entertainment/'},
            {'categories':'lifestyle','url':'http://sangbadpratidin.in/category/life-style/'},          
            
        ],     
    },

    'eisamay': {
        'name':'eisamay',
        'handaler_info':eisamay.get_artical_info,
        'handaler_list':eisamay.get_all_artical_links,
        'seeds':[
            {'categories':'calcutta','url':'http://eisamay.indiatimes.com/city/articlelist/15819618.cms'},
            {'categories':'state','url':'http://eisamay.indiatimes.com/state/articlelist/15819609.cms'},
            {'categories':'national','url':'http://eisamay.indiatimes.com/nation/articlelist/15819599.cms'},
            {'categories':'international','url':'http://eisamay.indiatimes.com/international/articlelist/15819594.cms'},
            {'categories':'entertainment','url':'http://eisamay.indiatimes.com/entertainment/articlelist/15819570.cms'},
            {'categories':'lifestyle','url':'http://eisamay.indiatimes.com/lifestyle/articlelist/15992436.cms'},          
            
        ],     
    },
    'oneindia': {
        'name':'oneindia',
        'handaler_info':oneindia.parse_artical,
        'handaler_list':oneindia.parse_artical_list,
        'seeds':[
            {'categories':'firstpage','url':'http://bengali.oneindia.com/news/','parser':oneindia.parse_first_page_list,'max_count':100},
            #{'categories':'science','url':''},
            {'categories':'calcutta','url':'http://bengali.oneindia.com/news/kolkata/'},
            {'categories':'state','url':'http://bengali.oneindia.com/news/west-bengal/'},
            {'categories':'national','url':'http://bengali.oneindia.com/news/india/'},
            {'categories':'international','url':'http://bengali.oneindia.com/news/international/'},
            {'categories':'sports','url':'http://bengali.oneindia.com/news/sports/'},
            {'categories':'entertainment','url':'http://bengali.oneindia.com/movies/'},
            #{'categories':'business','url':''},
            {'categories':'lifestyle','url':'http://bengali.oneindia.com/lifestyle/'},
            #{'categories':'girls','url':''},           
            {'categories':'travel','url':'http://bengali.oneindia.com/travel/'},           
            {'categories':'features','url':'http://bengali.oneindia.com/news/features/'},           
            {'categories':'cookery','url':'http://bengali.oneindia.com/cookery/'}, 
        ],    
    }, 
    'healthbarta': {
        'name':'healthbarta',
        'handaler_info':healthbarta.parse_artical,
        'handaler_list':healthbarta.parse_artical_list,
        'seeds':[
            {'categories':'firstpage','url':'http://www.healthbarta.com/','parser':healthbarta.parse_first_page_list,'max_count':100},
            {'categories':'food','url':'http://www.healthbarta.com/food-nutrition/'},
            {'categories':'women','url':'http://www.healthbarta.com/womens-health/'},
            {'categories':'men','url':'http://www.healthbarta.com/mens-health/'},
            {'categories':'beauty','url':'http://www.healthbarta.com/health-and-beauty/'},
            {'categories':'disease','url':'http://www.healthbarta.com/disease/'},
            {'categories':'relationships','url':'http://www.healthbarta.com/love-relationships/'},
            {'categories':'treatment','url':'http://www.healthbarta.com/family-treatment/'},
        ],    
    },
}