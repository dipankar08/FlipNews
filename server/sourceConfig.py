import ABP
import zeenews
import pratidin
import bartaman

master_config={
    'allowled':['sangbadpratidin','anandabazar','zeenews'],
    'max_news_in_each_cata':10,
}

config = {
    'bartaman': {
        'title':'hello',
        'handaler':bartaman.get_all_data_for_a_seed,
        'seeds':[ 
            {'categories':'calcutta','url':'http://zeenews.india.com/bengali/kolkata.html'},
            {'categories':'state','url':'http://zeenews.india.com/bengali/zila.html'},
            {'categories':'national','url':'http://zeenews.india.com/bengali/nation.html'},
            {'categories':'international','url':'http://zeenews.india.com/bengali/world.html'},
            {'categories':'lifestyle','url':'http://zeenews.india.com/bengali/lifestyle'},
        ],       
    
    },
    'anandabazar': {
        'title':'hello',
        'handaler':ABP.get_all_data_for_a_seed,
        'seeds':[
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
        'title':'hello',
        'handaler':zeenews.get_all_data_for_a_seed,
        'seeds':[ 
            {'categories':'calcutta','url':'http://zeenews.india.com/bengali/kolkata.html'},
            {'categories':'state','url':'http://zeenews.india.com/bengali/zila.html'},
            {'categories':'national','url':'http://zeenews.india.com/bengali/nation.html'},
            {'categories':'international','url':'http://zeenews.india.com/bengali/world.html'},
            {'categories':'lifestyle','url':'http://zeenews.india.com/bengali/lifestyle'},
        ],       
    
    },
    'sangbadpratidin': {
        'title':'hello',
        'handaler':pratidin.get_all_data_for_a_seed,
        'seeds':[
            {'categories':'calcutta','url':'http://sangbadpratidin.in/category/kolkata/'},
            {'categories':'state','url':'http://sangbadpratidin.in/category/state/'},
            {'categories':'national','url':'http://sangbadpratidin.in/category/country/'},
            {'categories':'international','url':'http://sangbadpratidin.in/category/international/'},
            {'categories':'entertainment','url':'http://sangbadpratidin.in/category/film-entertainment/'},
            {'categories':'lifestyle','url':'http://sangbadpratidin.in/category/life-style/'},          
            
        ],       
    
    },
}