require 'sinatra'


# gem 'activerecord', '=3.0.3'
# gem 'activerecord-jdbcsqlite3-adapter', '=1.1.2'  

require 'active_record'
require 'active_record/connection_adapters/jdbcsqlite3_adapter'   # needed? better way to specify??
require 'jdbc/sqlite3'   # needed? better way to specify??


ActiveRecord::Base.establish_connection(
    :adapter  => 'sqlite3',
    :database => 'links.sqlite3' )


#####################
# Models 

class Link < ActiveRecord::Base

  # self.table_name = 'links'
 
  attr_accessor :score 

  def recalc_score
    time_elapsed = (Time.now - self.created_at) / 3600    ## in hours?
    self.score = ((self.points-1) / (time_elapsed+2)**1.8)  ## .real
  end
 
  def self.hot
    self.all.each { |rec| rec.recalc_score }.sort { |l,r| l.score <=> r.score }.reverse
  end
  
  def url_host
    uri = URI.parse( url )
    uri.host
  end

end # class Link


##############################################
# Controllers / Routing / Request Handlers

get '/' do
  @links = Link.order( 'created_at desc' ).all
  erb :index
end

get '/hot' do 
  @links = Link.hot
  erb :index
end

post '/' do 
  l = Link.new( :title => params[:title], :url => params[:url] )
  l.save!
  redirect back  
end

put '/:id/vote/:type' do
  l = Link.find( params[:id] )
  l.points += params[:type].to_i
  l.save!
  redirect back
end
  

################################
# Views

__END__

@@layout
<!DOCTYPE html>
<html>
<head>
  <title>Bookmarks</title>
  <style>

body {
  font-family: arial,sans-serif;
  color: #222; 
}

form { display: inline; }

a {
  text-decoration: none;
  color: #12C;
}


h1.title { text-align: center;
           margin-bottom: 5px;
          }

.nav { text-align: center;
       margin-bottom: 20px; }

  
#links .points {
  font-size: 80%;
  padding-left: 25px;
  padding-right: 10px;
}  

#links .title {
 font-size: 120%;
 font-weight:  bold;
 padding-left: 30px;
}

#links .host,
#links .created-at {
 font-size: 80%;
 color: grey;
}

#post-link {
  margin-top: 25px;
}  

  </style>
</head>
<body>

<h1 class='title'>Bookmarks</h1>

<div class='nav'>
  <a href='/'>New</a> | <a href='/hot'>Hot</a>
</div>

<%= yield %>

</body>
</html>


@@index
<table id='links'>
<% @links.each do |l| %>
<tr>
  <td class='points'>
    <form action='<%= "#{l.id}/vote/1" %>' method='post'>
      <input type='hidden' name='_method' value='put'>
      <input type='submit' value='+1'>
    </form>    
    <%= l.points %>
    <form action='<%= "#{l.id}/vote/-1" %>' method='post'>
      <input type='hidden' name='_method' value='put'>
      <input type='submit' value='-1'>
    </form>    
  </td>
  <td><span class='title'><a href='<%= l.url %>'><%= l.title %></a></span>
      <span class='host'>(<%= l.url_host %>)</span>
      <span class='created-at'>posted <%= l.created_at %></span>
  </td>
</tr>
<% end %>
</table>

<div id='post-link'>
  <form action='/' method='post'>
    <input type='text' name='title' placeholder='Title'>
    <input type='text' name='url'   placeholder='URL'>
    <input type='submit' value='Save Link'>
  </form>  
</div>