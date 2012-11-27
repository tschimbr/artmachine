Amachine::Application.routes.draw do
   resources :compare do
     get :compare, :on => :collection
   end

  root  :to => "compare#compare"
end
