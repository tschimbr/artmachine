Amachine::Application.routes.draw do
   resources :compare do
     get :compare, :on => :collection
     get :stats, :on => :collection
   end

  root  :to => "compare#compare"
end
