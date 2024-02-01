var daweb_helpers = 
{
    eq: function (a, b) {
      return a === b;
    },
    times: function(n, block) {
        n = Math.trunc(n);
        var accum = '';
        for(var i = 0; i < n; ++i)
            accum += block.fn(i);
        return accum;
    },
    sum: function(a,b){
      return Number(a) + Number(b);
    },
    minus: function(a, b) {
      a = Math.trunc(a);
      b = Math.trunc(b);
      // Importante, trunca el numero...

      return Number(a) - Number(b);
    },
    twoDecimals: function(n){
      return n.toFixed(2);
    },
    takeOnly: function(array,n){
      return array.slice(0,n);
    },
    toJSON: function(object){
      return JSON.stringify(object);
    },
    formatDate: function(date){
      const dateObject = new Date(date);
      const day = dateObject.getDate();
      const month = dateObject.getMonth() + 1; // Los meses en JavaScript son base 0
      const year = dateObject.getFullYear();

      return `${day}/${month}/${year}`;
    }
};

module.exports = daweb_helpers;